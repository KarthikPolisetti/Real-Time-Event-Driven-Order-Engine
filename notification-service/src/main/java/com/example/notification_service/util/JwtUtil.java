package com.example.notification_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // THE CRYPTOGRAPHIC INK
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    // 1. THE PRINTER
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // --- NEW SCANNER FEATURES BELOW ---

    // 2. THE SCANNER: Reads the name off the wristband
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // We use the same ink to verify it wasn't forged
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. THE VALIDATOR: Checks if the wristband belongs to the person, and hasn't expired
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 4. THE TIMER CHECK: Is the 1 hour up?
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}