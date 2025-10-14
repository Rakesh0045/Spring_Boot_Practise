package com.tastytown.backend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tastytown.backend.constants.Role;
import com.tastytown.backend.dto.AuthRequest;
import com.tastytown.backend.dto.AuthResponse;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.repository.UserRepository;
import com.tastytown.backend.security.jwt.JwtUtils;
import com.tastytown.backend.service.IAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtUtils jwtUtils;

    @Override
    public User register(AuthRequest request) {
       if(userRepository.findByUserEmail(request.userEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
       }

       User user = User.builder()
                    .userEmail(request.userEmail())
                    .userPassword(encoder.encode(request.userPassword()))
                    .role(Role.ROLE_USER)
                    .build();

        var savedUser = userRepository.save(user);

        return savedUser;

        /*
         
            🧠 Flow:
         
            *   Checks if email is already registered using userRepository.
            *   If not, builds a new User object with:
                                                        email
                                                        encrypted password using BCryptPasswordEncoder
                                                        role ROLE_USER (default for all)

            * Saves the user in DB.
            * Returns the saved user.


         */
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        manager.authenticate(new UsernamePasswordAuthenticationToken(request.userEmail(), request.userPassword()));

        var user = userRepository.findByUserEmail(request.userEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+request.userEmail()));

        String token = jwtUtils.generateToken(user.getUserId(), user.getRole().toString());

        return new AuthResponse(token);

        /*
         
            🧠 Flow:

            * AuthenticationManager uses the email/password to authenticate:
            
            * If wrong → Spring Security throws error.
              If correct → flow continues.

            * Fetches the user from the database.
            
            * Calls jwtUtils.generateToken(...) to generate a JWT token for valid user.

            * Returns the token inside AuthResponse.

         */

    }

    @Override
    public User register(AuthRequest request, Role role) {
    if (userRepository.findByUserEmail(request.userEmail()).isPresent()) {
        throw new RuntimeException("Email already registered");
    }

    User user = User.builder()
        .userEmail(request.userEmail())
        .userPassword(encoder.encode(request.userPassword()))
        .role(role)
        .build();

    return userRepository.save(user);
}

    
}
