package com.zonasos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

@Service
public class JwtService {

    // Lee la clave desde application.properties (jwt.secret)
    @Value("${jwt.secret}")
    private String secretString;

    // Lee la expiracion en minutos desde application.properties (jwt.expiration-minutes)
    @Value("${jwt.expiration-minutes}")
    private long expirationMinutes;

    // Cachea la Key una vez construida (evita recalcularla en cada llamada)
    private Key secretKey;

    private Key getSecretKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Método para el login tradicional (con UserDetails)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return createToken(claims, userDetails.getUsername());
    }

    // 🌟 NUEVO MÉTODO PARA OAUTH (Google/GitHub)
    public String generateTokenForOAuth(String email, String rol) {
        Map<String, Object> claims = new HashMap<>();
        // Si el rol no empieza por ROLE_, se lo añadimos
        String roleWithPrefix = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;
        claims.put("role", roleWithPrefix);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long expirationMs = expirationMinutes * 60_000L; // minutos -> milisegundos
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}