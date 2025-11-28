package org.example.springjwt.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springjwt.entity.PasswordResetToken;
import org.example.springjwt.entity.User;
import org.example.springjwt.repository.PasswordResetTokenRepository;
import org.example.springjwt.repository.UserRepository;
import org.example.springjwt.utils.PasswordResetTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PasswordResetService
 *
 * Responsibilities:
 *  - Create password reset tokens and email the raw token to the user (forgot-password flow).
 *  - Validate a raw token and reset the user's password (reset-password flow).
 *
 * Security/Design notes:
 *  - Raw tokens are generated and emailed once; only the hash is stored in DB.
 *  - Tokens are single-use. Previous valid tokens are marked used before issuing a new one.
 *  - Methods are transactional to ensure DB state stays consistent when updates happen.
 *
 * ASCII FLOW (high-level):
 *
 *   Client (POST /auth/forgot-password { email })
 *       |
 *       v
 *   AuthController -> passwordResetService.createResetToken(email)
 *       - find user (silent on missing user)
 *       - mark old tokens used
 *       - generate rawToken, store hashed token
 *       - email rawToken to user
 *
 *   Client receives email -> user copies token
 *
 *   Client (POST /auth/reset-password { token, newPassword })
 *       |
 *       v
 *   AuthController -> passwordResetService.resetPassword(token, newPassword)
 *       - hash incoming token
 *       - lookup hashed token
 *       - validate not used && not expired && user exists
 *       - set new encoded password on user
 *       - mark token used and optionally invalidate other tokens
 *
 */
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenUtil tokenUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    /**
     * Create a password reset token for the given email and send it by email.
     *
     * Behavior:
     *  - If email not found -> returns silently (prevents account enumeration).
     *  - Marks previously active tokens for this user as used.
     *  - Creates and stores a new hashed token entry (single-use).
     *  - Sends the raw token to the user's email (raw token is NOT stored).
     *
     * @param email The email address to send reset instructions to.
     */
    @Transactional
    public void createResetToken(String email) {
        // --- Step 1: Lookup user (silent behavior if not found) ---
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Do not reveal anything to caller — just return.
            // Logging could reveal existence, so avoid logging the email here.
            return;
        }

        // --- Step 2: Invalidate any previous active tokens for this user ---
        List<PasswordResetToken> activeTokens =
                passwordResetTokenRepository.findAllByUserAndUsedFalseAndExpiresAtAfter(user, LocalDateTime.now());

        if (!activeTokens.isEmpty()) {
            // Mark them used to prevent multiple active tokens.
            activeTokens.forEach(t -> t.setUsed(true));
            passwordResetTokenRepository.saveAll(activeTokens);
        }

        // --- Step 3: Generate raw token and hash it for storage ---
        String rawToken = tokenUtil.generateRawToken();   // random URL-safe string
        String hashed = tokenUtil.hashToken(rawToken);    // store hashed representation

        // --- Step 4: Persist the token metadata (only hash stored) ---
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(hashed);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15)); // 15 min expiry
        token.setUsed(false);

        passwordResetTokenRepository.save(token);

        // --- Step 5: Send raw token to user's email ---
        // The raw token is sent once by email. Never log it.
        emailService.sendPasswordResetEmail(user, rawToken);

        log.info("Password reset token created and emailed for user id={}", user.getId());
    }

    /**
     * Reset the password using the raw token sent to user email earlier.
     *
     * Steps:
     *  - Validate inputs
     *  - Hash provided raw token and lookup DB row
     *  - Validate token state: exists, not expired, not used, has associated user
     *  - Encode & save the new password on the user
     *  - Mark the token as used and save
     *  - Invalidate other outstanding tokens for the same user
     *
     * @param rawToken   the token string the user received by email
     * @param newPassword the new password to set (plain text here; will be encoded)
     * @throws IllegalArgumentException on invalid input or invalid/expired token
     */
    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        // Input validation (fail fast)
        if (rawToken == null || newPassword == null || rawToken.isBlank() || newPassword.isBlank()) {
            throw new IllegalArgumentException("Token and new password must be provided.");
        }

        // Convert the raw token to the same hashed form we store
        String hashedToken = tokenUtil.hashToken(rawToken);

        // Lookup stored token by hash
        Optional<PasswordResetToken> opt = passwordResetTokenRepository.findByTokenHash(hashedToken);

        if (opt.isEmpty()) {
            // Token not found -> treat as invalid/expired.
            log.warn("Password reset attempted with unknown token hash.");
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        PasswordResetToken passwordResetToken = opt.get();

        // Retrieve user from token (LAZY): ensure a user exists
        User user = passwordResetToken.getUser();
        if (user == null) {
            log.warn("Password reset token found but no user attached (token id={}).", passwordResetToken.getId());
            throw new IllegalArgumentException("Invalid token.");
        }

        // Validate token usage/expiry
        LocalDateTime now = LocalDateTime.now();
        if (passwordResetToken.isUsed()) {
            log.warn("Attempt to reuse password reset token (token id={}).", passwordResetToken.getId());
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        if (passwordResetToken.getExpiresAt() == null || passwordResetToken.getExpiresAt().isBefore(now)) {
            log.warn("Attempt to use expired password reset token (token id={}).", passwordResetToken.getId());
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        // ===== Token valid: perform password change =====

        // 1) Encode & save the new password for the user
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 2) Mark this token used (single-use) and persist
        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);

        // 3) Invalidate other outstanding tokens for the user (defense-in-depth)
        List<PasswordResetToken> otherActive = passwordResetTokenRepository
                .findAllByUserAndUsedFalseAndExpiresAtAfter(user, now);

        // Mark other tokens used (skip the current token if present)
        for (PasswordResetToken t : otherActive) {
            if (!t.getId().equals(passwordResetToken.getId())) {
                t.setUsed(true);
            }
        }
        if (!otherActive.isEmpty()) {
            passwordResetTokenRepository.saveAll(otherActive);
        }

        // 4) Log success (do not log tokens or passwords)
        log.info("Password reset successful for user id={}", user.getId());

    }

}
