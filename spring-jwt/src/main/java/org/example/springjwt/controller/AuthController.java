package org.example.springjwt.controller;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.*;
import org.example.springjwt.entity.User;
import org.example.springjwt.service.AuthenticationService;
import org.example.springjwt.service.JwtService;
import org.example.springjwt.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    /**
     * User Registration Endpoint.
     *
     * Steps:
     * 1. Accepts user registration details (username, email, password).
     * 2. Calls signup() which:
     *      - saves the user with encoded password,
     *      - generates verification code,
     *      - sends email with code,
     *      - marks account disabled until verified.
     * 3. Returns the saved user object (for testing).
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * User Login Endpoint.
     *
     * Steps:
     * 1. Accepts email + password.
     * 2. Calls login(), which:
     *      - checks if user exists,
     *      - checks if account is verified,
     *      - authenticates credentials.
     * 3. If authentication succeeds:
     *      - generate JWT token,
     *      - send token + expiry as response.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.login(loginUserDto);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponseDto loginResponseDto = new LoginResponseDto(token, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponseDto);
    }

    /**
     * Email Verification Endpoint.
     *
     * Steps:
     * 1. Accepts email + OTP code.
     * 2. Calls verifyUser(), which:
     *      - checks if email exists,
     *      - checks if OTP expired,
     *      - matches OTP,
     *      - enables the user.
     * 3. Returns success or error message.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Resend Verification Code.
     *
     * Steps:
     * 1. Accepts email.
     * 2. Generates a new OTP.
     * 3. Sends verification email again.
     * 4. Returns success or error response.
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Forgot Password Endpoint.
     *
     * Steps:
     * 1. Accepts email in request.
     * 2. Calls createResetToken(), which:
     *      - silently checks if user exists,
     *      - invalidates previous valid tokens,
     *      - generates a secure random token,
     *      - hashes and stores it,
     *      - emails the raw token to user.
     * 3. Always returns a generic message (never leaks whether email exists).
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.createResetToken(request.getEmail());
        } catch (Exception ignored) {}

        return ResponseEntity.ok("If the email exists, password reset instructions have been sent.");
    }


    /**
     * Reset Password Endpoint.
     *
     * Steps:
     * 1. Accepts raw token + new password.
     * 2. Calls resetPassword(), which:
     *      - hashes raw token,
     *      - looks up matching stored hash,
     *      - checks expiry and "used" status,
     *      - updates user password if valid,
     *      - marks token as used.
     * 3. Response NEVER reveals validity of the token.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            passwordResetService.resetPassword(resetPasswordRequest.getToken(),
                    resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok("If the token is valid, the password has been reset.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid or expired token / invalid input.");
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(getClass()).error("Unexpected error during password reset", e);
            return ResponseEntity.status(500).body("Server error while resetting password.");
        }
    }





}


/*

                             +----------------+
                             |     Client     |
                             +----------------+
                                      |
       Signup ------------------------|
       | POST /auth/signup           |
                                      v
                          +-----------------------------+
                          |        AuthController       |
                          +-----------------------------+
                                      |
                                      v
                          +-----------------------------+
                          |   AuthenticationService     |
                          | - create user (enabled=false)
                          | - generate verification code
                          | - send verification email
                          +-----------------------------+
                                      |
                             User verifies email
                                      |
              POST /auth/verify ------------------------>
                                      |
                                      v
                          +-----------------------------+
                          |   AuthenticationService     |
                          | - verify OTP + enable user  |
                          +-----------------------------+

-------------- LOGIN FLOW --------------
POST /auth/login {email,password}
        |
        v
+----------------------+     +----------------+
| AuthenticationService| --> |   JwtService   |
+----------------------+     +----------------+
        |
        v
Returns JWT token


----------- FORGOT PASSWORD FLOW -----------
Client: POST /auth/forgot-password {email}
        |
        v
+-----------------------------+
|   PasswordResetService      |
| - find user silently        |
| - invalidate old tokens     |
| - generate raw token        |
| - hash + save token         |
| - send email with rawToken  |
+-----------------------------+


----------- RESET PASSWORD FLOW -----------
Client: POST /auth/reset-password {token,newPassword}
        |
        v
+-----------------------------+
|   PasswordResetService      |
| - hash raw token            |
| - find matching tokenHash   |
| - validate expiry + used    |
| - update user password      |
| - mark token as used        |
+-----------------------------+


----------- PROTECTED RESOURCES -----------
All other endpoints require:
Authorization: Bearer <jwt>



 */
