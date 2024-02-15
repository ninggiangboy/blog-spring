package dev.ngb.blog_spring.token;

import dev.ngb.blog_spring.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public interface JwtService {
    UUID extractUserId(String token);

    Collection<SimpleGrantedAuthority> extractAuthorities(String token);

    String generateToken(User user, HttpServletRequest request);

    boolean isTokenValid(String token, HttpServletRequest request);
}
