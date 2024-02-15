package dev.ngb.blog_spring.auth;

import dev.ngb.blog_spring.auth.domain.AuthRequest;
import dev.ngb.blog_spring.auth.domain.AuthResponse;
import dev.ngb.blog_spring.user.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest, HttpServletRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletRequest request);

    void logout(String refreshToken, User user);

    void logoutAll(User user);
}
