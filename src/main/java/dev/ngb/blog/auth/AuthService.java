package dev.ngb.blog.auth;

import dev.ngb.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest, HttpServletRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletRequest request);

    void logout(String refreshToken);

    void logoutAll(User user);
}
