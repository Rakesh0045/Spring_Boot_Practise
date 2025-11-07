package com.tastytown.backend.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tastytown.backend.entity.User;
import com.tastytown.backend.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
/*

    This class is a Spring Security filter that runs before each request, checking:

        If a JWT is present in the header.

        If valid → extract user and set authentication in the Spring Security context.

 */
public class JwtFilter extends OncePerRequestFilter { // OncePerRequestFilter → Ensures this filter runs once per request.

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        try{
            if(authHeader == null || !authHeader.startsWith("Bearer")){
                filterChain.doFilter(request, response); // let spring security handles unauthorized access
                return;
            }

            String token = authHeader.substring(7);
            String userId = jwtUtils.getUserId(token);

            // SecurityContextHolder --> Stores authenticated user info for Spring Security & manages everything
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){

                //extract user obj for extracting the role 
                User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("user not found with id: "+userId));

                //extract role from user obj
                var roles = List.of(new SimpleGrantedAuthority(user.getRole().toString()));

                //create authentication obj which contains user info. along with its roles
                //Creating a security context (UsernamePasswordAuthenticationToken) with your logged-in user and their roles.
                var authentication = new UsernamePasswordAuthenticationToken(user, null, roles);

                //store the authentication object inside the security context
                //Putting it into the current thread’s SecurityContextHolder.
                SecurityContextHolder.getContext().setAuthentication(authentication);


                /*

                        🛡️ What is SecurityContextHolder?

                            SecurityContextHolder is a thread-local storage container used by Spring Security to store security-related information (like the currently authenticated user) for the duration of a request.

                        🧠 Think of it like:

                        A secure box that holds the current user's login details, roles, and authentication status — available to the entire backend while a request is being handled.

                        📦 What does it store?
                        It stores a SecurityContext object, which holds an Authentication object.

                        SecurityContextHolder
                            |
                            └── SecurityContext
                                    |
                                    └── Authentication

                        ✅ The Authentication object includes:
                        
                        Principal → the user object (can be your User, UserDetails, etc.)

                        Credentials → usually null (we don’t store passwords after login)

                        Authorities → list of roles or permissions like ROLE_ADMIN, ROLE_USER

                        isAuthenticated → boolean flag

                 */
            }

            request.setAttribute("userId", userId); //for services/controllers to use --> once request hits and verified & authenticated, setAttribute userId which can be used anywhere if user has logged in

            filterChain.doFilter(request, response);

        }catch(Exception e){
            var problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            problemDetail.setTitle("Invalid Token");
            problemDetail.setDetail(e.getMessage());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(problemDetail);
        }

    }
    
}

/*
                         [Frontend Request]
                                |
                                v
                    ┌────────────────────────┐
                    │  JwtFilter.doFilter()  │
                    └────────────────────────┘
                                |
                        Check "Authorization" header
                                |
                        Valid "Bearer" Token?
                                |
                            Yes     No
                            |        |
                            v        v
                    Parse JWT      Allow as unauthenticated
                        |
                    Extract userId
                        |
                    Load user from DB
                        |
                    Create Authentication object
                        |
                    Set it in SecurityContextHolder
                        |
                    Proceed to Controller/Service

 */
