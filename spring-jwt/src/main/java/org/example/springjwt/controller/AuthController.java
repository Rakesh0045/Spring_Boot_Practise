package org.example.springjwt.controller;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.LoginResponseDto;
import org.example.springjwt.dto.LoginUserDto;
import org.example.springjwt.dto.RegisterUserDto;
import org.example.springjwt.dto.VerifyUserDto;
import org.example.springjwt.entity.User;
import org.example.springjwt.service.AuthenticationService;
import org.example.springjwt.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

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
}
