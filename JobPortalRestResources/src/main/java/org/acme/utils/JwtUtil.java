package org.acme.utils;

import java.time.Duration;
import java.util.Set;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
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

    @Inject
    JWTParser jwtParser;

    public JsonWebToken parseToken(String token) throws ParseException {
        return jwtParser.parse(token);
    }
}
