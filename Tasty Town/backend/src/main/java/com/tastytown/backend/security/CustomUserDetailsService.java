package com.tastytown.backend.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.tastytown.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // UserDetailsService -->  Interface Spring uses to fetch user info

    /*
     
    This class:

        Finds the user from DB using their email

        Wraps their info in Spring’s UserDetails object

        Passes that info to AuthenticationManager to validate credentials --> DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

     */

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var user = userRepository.findByUserEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not exist with email: "+email));

        return new User(
            user.getUserEmail(),
            user.getUserPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }
    
}

/*

    Frontend calls POST /login with email + password
                       ↓
     Spring Security calls CustomUserDetailsService.loadUserByUsername()
                       ↓
    Gets user email + encoded password + roles
                       ↓
     Matches password using PasswordEncoder
                       ↓
             If valid → generate JWT

 */
