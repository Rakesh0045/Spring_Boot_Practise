package com.rakesh.spring_keycloak_demo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // 1. Start the chain
                .authorizeHttpRequests(auth -> auth // 2. Configure authorization
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2 // 3. Configure as Oauth2 resource server
                        .jwt(jwt -> jwt // 4. START configuring JWT properties
                                .jwtAuthenticationConverter(jwtAuthConverter) // 5. APPLY the custom converter here
                        ) // 6. END JWT configuration
                )
                .sessionManagement(session -> session // 7. Configure session policy
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ); // 8. End of chain

        return httpSecurity.build(); // 9. Build the final object
    }
}
