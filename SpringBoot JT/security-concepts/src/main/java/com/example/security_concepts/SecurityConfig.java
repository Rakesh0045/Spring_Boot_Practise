package com.example.security_concepts;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/about").permitAll()
                .anyRequest().authenticated())
                .httpBasic(httpBasic -> {}) //applies default security settings
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(detailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    UserDetailsService detailsService() {
        return email -> {
            //return new User("Rakesh","{noop}1234",List.of());

            User user = userRepository.findByUserEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));

            return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                List.of()
            );
        };
    }


    /*
     
    * BCrypt Encoding generates a hash value for the given password
    
    * It hashes the given password string specified no of times
    
    * It never decrypts the password. 
    
    * It just encrypts/hashes the given password String and matches with the hashed value of original password saved in DB

     */

    @Bean 
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
