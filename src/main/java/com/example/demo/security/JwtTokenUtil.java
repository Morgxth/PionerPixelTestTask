package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 день
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            extractUserId(token); return true;
        } catch (Exception e) {
            return false;
        }
    }
}
