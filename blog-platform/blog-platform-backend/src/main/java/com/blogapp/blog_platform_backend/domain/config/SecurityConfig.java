package com.blogapp.blog_platform_backend.domain.config;

import com.blogapp.blog_platform_backend.domain.entities.User;
import com.blogapp.blog_platform_backend.domain.repositories.UserRepository;
import com.blogapp.blog_platform_backend.domain.security.BlogUserDetailsService;
import com.blogapp.blog_platform_backend.domain.security.JwtAuthenticationFilter;
import com.blogapp.blog_platform_backend.domain.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepository);

        String email = "user@test.com";
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .name("Test User")
                    .email(email)
                    .password(passwordEncoder().encode("password"))
                    .build();
            return userRepository.save(newUser);
        });

        return blogUserDetailsService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // This allows your frontend at localhost:5173 to make requests
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


}



/*

🔸 Login Flow

        Client → /api/v1/auth/login
           ↓
        AuthController
           ↓
        AuthenticationService.authenticate()
           ↓
        AuthenticationManager → BlogUserDetailsService → DB User
           ↓
        JWT generated
           ↓
        Response (token)

🔸 Protected API Flow

        Client → /api/v1/posts
           ↓
        JwtAuthenticationFilter (extracts token)
           ↓
        validateToken() → load user → set SecurityContext
           ↓
        Spring Security knows user → Controller executes
           ↓
        Response returned






🌐 1️⃣ LOGIN REQUEST FLOW

        Goal: User sends credentials → server verifies → returns JWT token

        🔹 Step-by-Step Flow

                Client sends request

                POST /api/v1/auth/login
                Body: { "email": "user@test.com", "password": "password" }


                ↓
                Request hits your backend.

                Spring Security sees this URL

                In SecurityConfig, /api/v1/auth/login is marked as permitAll().

                ✅ So Spring does not block it; it goes directly to your controller.

                Controller handles it

                AuthController → login()


                Calls authenticationService.authenticate(email, password)

                AuthenticationServiceImpl.authenticate()

                Uses AuthenticationManager to check if the credentials are valid.

                Internally:

                AuthenticationManager → uses UserDetailsService (BlogUserDetailsService)

                Loads user from DB using email

                Compares password using PasswordEncoder

                ✅ If valid → returns UserDetails

                Token generation

                Controller then calls authenticationService.generateToken(userDetails)

                Generates a signed JWT using:

                Username

                Expiration time

                Secret key

                Response sent

                {
                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
                  "expiresIn": 86400
                }


                Client saves this token in local storage.




🌐 2️⃣ AUTHENTICATED REQUEST FLOW

        Goal: Use JWT token to access protected resources

        🔹 Step-by-Step Flow

                Client sends request with token

                GET /api/v1/posts
                Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIs...


                Request enters Spring Security

                Every request passes through SecurityFilterChain → filters → JwtAuthenticationFilter.

                JwtAuthenticationFilter triggers

                Extracts token from header.

                If header missing → request continues as anonymous (unauthenticated).

                Token found → validate it

                JwtAuthenticationFilter calls:

                authenticationService.validateToken(token)


                Inside:

                Extracts username from token.

                Loads user from DB again using UserDetailsService.

                ✅ If token valid → returns UserDetails.

                Create Authentication object

                new UsernamePasswordAuthenticationToken(userDetails, null, authorities)


                Puts it inside:

                SecurityContextHolder.getContext().setAuthentication(authenticationToken)


                This means the user is now authenticated for this request.

                Filter chain continues

                Now Spring Security knows who the user is.

                Passes control to your controller method.

                Controller executes normally

                Example: /api/v1/posts/create

                If controller has @PreAuthorize or role-based checks, Spring uses this context to validate access.

                Response returned

                Controller returns the response.

                Request cycle completes.

 */
