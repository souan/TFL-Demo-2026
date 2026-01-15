package lu.cnfpcfullstackdev.tfl_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lu.cnfpcfullstackdev.tfl_api.entity.TflUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;  // in milliseconds
    
    // Generate JWT token for a user
    public String generateToken(TflUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole().toString());
        
        return Jwts.builder()
                .claims(claims)                                    // Custom data
                .subject(user.getUsername())                       // Standard subject claim
                .issuedAt(new Date())                              // When created
                .expiration(new Date(System.currentTimeMillis() + expiration))  // When expires
                .signWith(getSigningKey())                         // Sign with our secret
                .compact();                                        // Build the string
    }
    
    // Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    // Extract user ID from token
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }
    
    // Extract role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    
    // Validate token
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);       // This throws if signature is invalid
            return !isTokenExpired(token);  // Also check expiration
        } catch (Exception e) {
            return false;  // Any exception = invalid token
        }
    }
    
    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    
    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Verify signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    // Convert secret string to SecretKey
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}