package org.example.springjwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtService
 *
 * Responsibilities:
 *  - Generate signed JWTs (create token for authenticated users)
 *  - Parse tokens and extract claims
 *  - Validate token (subject + expiration)
 */

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /* ----------------------
       Token generation
       ---------------------- */

    /**
     * Create a token with default/empty extra claims.
     * call used by authentication controller after successful login.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    /**
     * Create a token with custom claims
     * extraClaims will be added to the token body.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Build the token: add claims, subject (username), issuedAt, expiration and sign.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationMillis) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiry = new Date(now + expirationMillis);

        return Jwts.builder()
                .setClaims(extraClaims)              // custom claims (roles, jti, etc)
                .setSubject(userDetails.getUsername()) // principal (username)
                .setIssuedAt(issuedAt)               // iat
                .setExpiration(expiry)               // exp
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign using HMAC-SHA256
                .compact();
    }

    /* ----------------------
       Token parsing / claims
       ---------------------- */

    /**
     * Extract the username (subject) from token.
     * Returns null / throws runtime JwtException if token invalid/expired.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generic method to extract any claim using a resolver function.
     * Example usage: extractClaim(token, Claims::getExpiration)
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse the token, validate signature and return all claims.
     * Throws JwtException (ExpiredJwtException, MalformedJwtException, etc) on problems.
     * We catch at a higher level where appropriate.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Convert configured base64 secret into a Key suited for HMAC signing.
     * Keep the secret safe and rotate periodically.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* ----------------------
       Token validation
       ---------------------- */

    /**
     * Validate token by checking:
     *  - the subject matches the given userDetails
     *  - token is not expired
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null
                    && username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
        } catch (JwtException e) {
            // token invalid or expired
            return false;
        }
    }

    /**
     * Check whether token's expiration date is before current time.
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            // treat parsing errors as expired/invalid
            return true;
        }
    }

    /**
     * Extract expiration Date object from token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /* ----------------------
       Helper getters
       ---------------------- */

    /**
     * Expose configured expiration for other classes.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }
}


/*
===============================================================================
 JWT FLOW (HIGH-LEVEL)
===============================================================================

   +-------------+         +----------------------+         +-----------------+
   |   Client    |         |  Authentication      |         |   JwtService    |
   | (Login Req) | ----->  |   Controller/Auth    | ----->  |  generateToken  |
   +-------------+         +----------------------+         +-----------------+
                                      |
                                      |  JWT created:
                                      |  - subject (username)
                                      |  - issuedAt
                                      |  - expiration
                                      |  - extra claims
                                      V
                             +------------------------+
                             |      Signed JWT        |
                             +------------------------+
                                      |
                                      V
   +-------------+         Sends JWT in Authorization Header
   |   Client    | ---------------------------------------->
   +-------------+         "Authorization: Bearer <token>"
                                      |
                                      V
   +------------------------------+
   |      JwtService.validateToken |
   +------------------------------+
        |             |
        |             +---> extractUsername(token)
        |                     |
        |                     +---> extractClaim(token)
        |                               |
        |                               +---> extractAllClaims(token)
        |                                           |
        |                                           +---> verify signature using key
        |
        +---> isTokenExpired(token)

   If signature is valid AND not expired AND username matches:
   --> Request is authenticated

===============================================================================
*/



/*
===================================================================================
 INTERNAL METHOD FLOW (DETAILED)
===================================================================================

  generateToken(userDetails)
        |
        ---> generateToken(extraClaims, userDetails)
                |
                ---> buildToken(extraClaims, userDetails, expiration)
                        |
                        ---> getSigningKey()
                        |
                        ---> Jwts.builder()
                                   .setClaims(...)
                                   .setSubject(...)
                                   .setIssuedAt(...)
                                   .setExpiration(...)
                                   .signWith(key, HS256)
                                   .compact()
                        |
                        ---> returns JWT string


  validateToken(token, userDetails)
        |
        ---> extractUsername(token)
                |
                ---> extractClaim(token, Claims::getSubject)
                        |
                        ---> extractAllClaims(token)
                                |
                                ---> Jwts.parserBuilder()
                                         .setSigningKey(key)
                                         .build()
                                         .parseClaimsJws(token)
                                         .getBody()
                                |
                                ---> returns Claims
        |
        ---> isTokenExpired(token)
                |
                ---> extractExpiration(token)
                        |
                        ---> extractClaim(token, Claims::getExpiration)
                                |
                                ---> extractAllClaims(token)
                                |
                                ---> get expiration date
        |
        ---> Compare username + expiration result
        |
        ---> return true/false


===================================================================================
*/
