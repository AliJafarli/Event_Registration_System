package com.codealpha.eventreg.security;

import com.codealpha.eventreg.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class JwtService {

    private final JwtProperties props;
    private final javax.crypto.SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        byte[] secretBytes = props.getSecret().getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTokenMinutes() * 60);

        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .claim("email", user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(props.getIssuer())
                .build()
                .parseSignedClaims(token);
    }

    public long getAccessTokenExpiresInSeconds() {
        return props.getAccessTokenMinutes() * 60;
    }
}
