package dev.ngb.blog.auth;

import dev.ngb.blog.user.User;

import java.util.UUID;

public interface JwtService {
    UUID extractUserId(String token);

    String generateToken(User user);

    boolean isTokenExpired(String token);
}
