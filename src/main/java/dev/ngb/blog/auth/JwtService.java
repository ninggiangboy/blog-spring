package dev.ngb.blog.auth;

import dev.ngb.blog.user.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public interface JwtService {
    UUID extractUserId(String token);

    Collection<SimpleGrantedAuthority> extractAuthorities(String token);

    String generateToken(User user);

    boolean isTokenExpired(String token);
}
