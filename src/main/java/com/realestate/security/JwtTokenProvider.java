package com.realestate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret:ThisIsAVerySecureSecretKeyForJWTTokenGenerationMinimum256Bits}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${spring.security.jwt.access-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${spring.security.jwt.refresh-expiration:604800000}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateAccessToken(userDetails.getUsername());
    }

    public String generateToken(String username) {
        return generateAccessToken(username);
    }

    public String generateAccessToken(String username) {
        return createToken(username, "access", accessTokenExpiration);
    }

    public String generateRefreshToken(String username, String familyId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("familyId", familyId);
        return createTokenWithClaims(claims, username, refreshTokenExpiration);
    }

    private String createToken(String subject, String tokenType, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", tokenType);
        return createTokenWithClaims(claims, subject, expiration);
    }

    private String createTokenWithClaims(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getJtiFromToken(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String getTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("type");
    }

    public String getFamilyId(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("familyId");
    }

    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    public boolean isAccessToken(String token) {
        try {
            String type = getTokenType(token);
            return "access".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            String type = getTokenType(token);
            return "refresh".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String refreshToken(String token) {
        if (validateToken(token) && !isTokenExpired(token)) {
            String username = getUsernameFromToken(token);
            return generateAccessToken(username);
        }
        return null;
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpiration;
    }
}
