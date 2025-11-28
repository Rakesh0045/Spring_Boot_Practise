package org.example.springjwt.utils;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class PasswordResetTokenUtil {

    // ===== Constants / singletons =====

    // Reuse a single SecureRandom instance for the JVM to avoid performance overhead of creating new instances.
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Size in bytes for the raw token. 32 bytes -> ~43-44 chars when base64url encoded.
    private static final int TOKEN_BYTE_LENGTH = 32;

    // Hash algorithm constant
    private static final String HASH_ALGO = "SHA-256";

    // ===== generateRawToken() =====
    /**
     * Generates a random, URL-safe string.
     * Note: Do not log or store this raw token except to send it once via email.
     */
    public String generateRawToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // ===== hashToken(String rawToken) =====
    /**
     * Hashes the raw token for secure storage.
     * We convert the rawToken into a digest and encode the digest using base64url. 
     * This value is safe to store in the DB.
     */
    public String hashToken(String rawToken) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
            byte[] digest = md.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            // Wrap in unchecked exception because SHA-256 is expected to exist on JVM.
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    // ===== matches(String rawToken, String storedHash) =====
    /**
     * Verifies if a raw token matches the stored hash.
     * USES TIMING-SAFE COMPARISON.
     * Note: Never compare encoded strings with equals(). 
     */
    public boolean matches(String rawToken, String storedHash) {
        try {
            // Compute digest bytes for the incoming rawToken
            MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
            byte[] digest = md.digest(rawToken.getBytes(StandardCharsets.UTF_8));

            // Decode storedHash to bytes using the decoder that matches your encoding
            byte[] storedBytes = Base64.getUrlDecoder().decode(storedHash);

            // Use MessageDigest.isEqual(digest, storedBytes) for timing-safe compare
            return MessageDigest.isEqual(digest, storedBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        } catch (IllegalArgumentException e) {
            // Decoding error (storedHash malformed) — treat as non-match
            return false;
        }
    }

    // ===== calculateExpiry(int minutes) =====
    /**
     * Helper to calculate expiry time from now.
     */
    public LocalDateTime calculateExpiry(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }

}