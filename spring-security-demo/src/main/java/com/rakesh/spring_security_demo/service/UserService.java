package com.rakesh.spring_security_demo.service;

import com.rakesh.spring_security_demo.entity.User;
import com.rakesh.spring_security_demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;


    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String verify(User user) {
        Authentication authenticate
                = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(), user.getPassword()
                )
        );

        //var u = userRepository.findByUserName(user.getUserName());
        if(authenticate.isAuthenticated())
            return jwtService.generateToken(user);
        return "failure";
    }
}
