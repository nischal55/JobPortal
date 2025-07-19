package org.acme.utils;

import java.time.Duration;
import java.util.Set;
import io.smallrye.jwt.build.Jwt;

public class JwtUtil {
    public static String generateAccessToken(String username, String role) {
        return Jwt.issuer("quarkus-jwt")
                .subject(username)
                .groups(Set.of(role))
                .expiresIn(Duration.ofMinutes(15))
                .sign();
    }

    public static String generateRefreshToken(String username, String role) {
        return Jwt.issuer("quarkus-jwt")
                .subject(username)
                .groups(Set.of(role))
                .expiresIn(Duration.ofDays(7))
                .sign();
    }
}
