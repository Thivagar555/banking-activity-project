package com.banking.BankingProject.security;

import com.banking.BankingProject.user.User; // Import User class
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Must be 32+ characters for HS256
    private static final String SECRET = "MY_SUPER_SECRET_BANKING_KEY_1234567890";

    // Token expiry (10 min)
    private static final long EXPIRATION_TIME = 20 * 60 * 1000;

    // Convert secret string to SecretKey (required for new version)
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ------------------------ Generate Token (MODIFIED) ------------------------
    // Now accepts a User object to pull the role.
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name()) // <-- NEW: Embed the user's role (ADMIN/USER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // ------------------------ Extract Email ------------------------
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ------------------------ Extract Role (NEW) ------------------------
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ------------------------ Parse Claims ------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ------------------------ Check Expiry ------------------------
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}