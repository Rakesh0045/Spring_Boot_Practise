package com.tastytown.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastytown.backend.constants.Role;
import com.tastytown.backend.dto.AuthRequest;
import com.tastytown.backend.dto.AuthResponse;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.service.IAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {
            "http://localhost:5173",
            "http://127.0.0.1:5173"
        }
    )
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody AuthRequest request){
        //Saves user, returns User object
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request){
        //Returns JWT token (AuthResponse) if successful
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<User> registerAdmin(@RequestBody AuthRequest request) {
        return new ResponseEntity<>(authService.register(request, Role.ROLE_ADMIN), HttpStatus.CREATED);
    }

}

