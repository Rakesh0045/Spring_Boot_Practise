package org.example.springjwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springjwt.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/*

1. Read Authorization header
2. Check Bearer format
3. Extract token
4. Extract username from token
5. Load userDetails
6. Validate token → set Authentication in SecurityContext

 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /*

            JWT FILTER FLOW
        -----------------------------------------
        Request --> JwtAuthFilter --> Controller

        Inside JwtAuthFilter:

        1) Get Authorization header
        2) If header missing or not Bearer → skip filter
        3) Extract token (string after "Bearer ")
        4) Use jwtService to extract username
        5) If username exists AND no existing authentication:
               a) Load userDetails from DB
               b) Validate JWT signature + expiry
               c) If valid → build UsernamePasswordAuthToken
               d) Put it inside SecurityContext
        6) Continue filter chain
        -----------------------------------------


     */

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        // ------------------------------------------------------------
        // 1️⃣ Extract the Authorization header
        // ------------------------------------------------------------
        final String authHeader = request.getHeader("Authorization");

        // If header is missing or doesn't start with "Bearer ", skip filter.
        // The request will simply proceed unauthenticated.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // ------------------------------------------------------------
            // 2️⃣ Extract the JWT (remove "Bearer ")
            // ------------------------------------------------------------
            final String jwt = authHeader.substring(7);

            // ------------------------------------------------------------
            // 3️⃣ Extract username (subject) from token
            // ------------------------------------------------------------
            final String userEmail = jwtService.extractUsername(jwt);

            // Check if SecurityContext is empty (user not authenticated yet)
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

            // ------------------------------------------------------------
            // 4️⃣ If username exists & user not already authenticated:
            // ------------------------------------------------------------
            if (userEmail != null && existingAuth == null) {

                // Load the user details from DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // ------------------------------------------------------------
                // 5️⃣ Validate JWT (signature, expiry, username)
                // ------------------------------------------------------------
                if (jwtService.validateToken(jwt, userDetails)) {

                    // ------------------------------------------------------------
                    // 6️⃣ Create authentication token & attach user authorities
                    // ------------------------------------------------------------
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Add request details (IP, session, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ------------------------------------------------------------
                    // 7️⃣ Store authentication in SecurityContext
                    // ------------------------------------------------------------
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // ------------------------------------------------------------
            // 8️⃣ Continue with filter chain — request is now authenticated
            // ------------------------------------------------------------
            chain.doFilter(request, response);

        } catch (Exception ex) {
            // ------------------------------------------------------------
            // ❌ If anything fails (invalid token, expired, signature error),
            //    let Spring's exception resolver handle it.
            // ------------------------------------------------------------
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}