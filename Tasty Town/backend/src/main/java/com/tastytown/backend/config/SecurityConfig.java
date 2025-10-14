package com.tastytown.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tastytown.backend.security.CustomUserDetailsService;
import com.tastytown.backend.security.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // No session; uses JWT instead (stateless auth).
                .cors(cors -> {
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/foods/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        .requestMatchers("/api/v1/auth/register-admin").hasRole("ADMIN")

                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers("/api/v1/cart/**").authenticated()
                        // all cart requests must be authenticated and no role based restrictions
                        // applied

                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/user").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/v1/foods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/foods/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/foods/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole("ADMIN")

                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/images/**").permitAll() 

                        .anyRequest().authenticated())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        // Compares password with encoded one.
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        // Returns AuthenticationManager to be used in login logic.
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        /*
         * 
         * BCrypt is a secure hashing algorithm with built-in salt.
         * 
         * Used in AuthServiceImpl.register() to encode passwords before saving.
         * 
         * Used in login flow to compare encoded passwords.
         * 
         */
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Allowed methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed headers
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Allow credentials (for cookies / Authorization header)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}

/*
 * 
 * ┌───────────────┐
 * Request ───▶ │ SecurityFilter│ ──────▶ Checks path and token
 * └──────┬────────┘
 * │
 * ┌────────▼──────────┐
 * │AuthenticationMgr │──▶ Validates credentials
 * └──────┬────────────┘
 * ▼
 * ┌──────────────┐
 * │UserDetailsSvc│──▶ Loads user from DB
 * └──────────────┘
 * 
 * 
 * 
 */
