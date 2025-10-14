package com.tastytown.backend.security.jwt;

import java.util.Base64;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    //require('crypto').randomBytes(32).toString('hex')
    private static final String JWT_SECRET = "716cdcf2fb0469e62fae24a7c943c4156c2c06caebf8dc3a106f7d940564802c";

    //Converts the base64-encoded string into a SecretKey used for signing and verification.
    private SecretKey getKey(){
        byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String userId, String role){
        return Jwts.builder()
                .subject(userId)
                .claim("role",role)
                .signWith(getKey())
                .compact();
        /*
            ✅ What it does:

            Sets the user ID as the subject.
            Adds the role as a claim.
            Signs the token with your secret key using HS256 algorithm.
            Returns the compact JWT token string.
        
        */        
    }

    public String getUserId(String token) { // verify token, claim username
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        /*
            Parses and verifies the token signature.
            Returns the subject field (which is your userId).
        */        
    }

    public String getUserRole(String token){
        //Extracts the role claim from the token payload.
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
    }
}
