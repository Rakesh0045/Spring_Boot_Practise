package org.example.springjwt.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.LoginUserDto;
import org.example.springjwt.dto.RegisterUserDto;
import org.example.springjwt.dto.VerifyUserDto;
import org.example.springjwt.entity.User;
import org.example.springjwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Injecting required dependencies through constructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    // ----------------------------------------
    // SIGNUP: Register new user + send OTP
    // ----------------------------------------
    public User signup(RegisterUserDto registerUserDto) {

        // Create new user object from DTO
        User user = new User(
                registerUserDto.getUsername(),
                registerUserDto.getEmail(),
                passwordEncoder.encode(registerUserDto.getPassword()) // Hash the password
        );

        // Generate a 6-digit verification code
        user.setVerificationCode(generateVerificationCode());

        // Set expiration time for 10 minutes
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));

        // Account stays disabled until verified
        user.setEnabled(false);

        // Send the verification email
        sendVerificationEmail(user);

        // Save user into DB (verification code + disabled status stored)
        return userRepository.save(user);
    }


    // ----------------------------------------
    // LOGIN: Authenticate verified user
    // ----------------------------------------
    public User login(LoginUserDto loginUserDto) {

        // Get user by email, or throw if not found
        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + loginUserDto.getEmail())
                );

        // If not verified, block login
        if(!user.isEnabled()) {
            throw new RuntimeException("Account not verified");
        }

        // Spring Security authentication (checks password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        // Return user if authentication succeeded
        return user;
    }


    // ----------------------------------------
    // VERIFY USER: Match OTP + enable account
    // ----------------------------------------
    public void verifyUser(VerifyUserDto verifyUserDto) {

        // Find user by email
        Optional<User> optionalUser = userRepository.findByEmail(verifyUserDto.getEmail());

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if OTP expired
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code expired");
            }

            // Match OTP
            if(user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {

                // Mark account verified
                user.setEnabled(true);

                // Clear OTP details
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);

                // Save updated user
                userRepository.save(user);

            } else {
                throw new RuntimeException("Verification code not valid");
            }

        } else  {
            throw new RuntimeException("User not found with email: " + verifyUserDto.getEmail());
        }
    }


    // ----------------------------------------
    // RESEND OTP: Regenerate + email again
    // ----------------------------------------
    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            // If already verified, no need to send again
            if(user.isEnabled()){
                throw new RuntimeException("Account is already verified");
            }

            // Generate new OTP and expiry
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));

            // Send new email and save
            sendVerificationEmail(user);
            userRepository.save(user);

        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }


    // ----------------------------------------
    // OTP GENERATOR (6 digits)
    // ----------------------------------------
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // ensures 100000–999999
        return String.valueOf(code);
    }


    // ----------------------------------------
    // SEND EMAIL: Builds HTML + calls EmailService
    // ----------------------------------------
    private void sendVerificationEmail(User user) {

        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();

        // HTML message for email
        String htmlMessage =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<title>Verification Code</title>" +
                        "</head>" +
                        "<body style='margin:0; padding:0; font-family:Arial, sans-serif; background:#f4f4f4;'>" +

                        "<div style='max-width:480px; margin:40px auto; background:#ffffff; border-radius:8px; " +
                        "padding:30px; box-shadow:0 4px 12px rgba(0,0,0,0.08);'>" +

                        "<h2 style='margin:0 0 10px; color:#222; font-size:22px; font-weight:600;'>Verify Your Email</h2>" +

                        "<p style='font-size:15px; line-height:1.6; color:#555;'>Use the verification code below to continue:</p>" +

                        "<div style='margin:25px 0; padding:18px; text-align:center; " +
                        "background:#f7f9ff; border:1px solid #dbe3ff; border-radius:6px;'>" +

                        "<span style='font-size:26px; font-weight:700; color:#3b5bdb; letter-spacing:3px;'>" +
                        verificationCode + "</span>" +
                        "</div>" +

                        "<p style='font-size:13px; color:#888;'>If you didn't request this, please ignore this email.</p>" +

                        "</div>" +
                        "</body>" +
                        "</html>";

        // Attempt to send mail
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // for debugging — replace with logger in production
        }
    }
}
