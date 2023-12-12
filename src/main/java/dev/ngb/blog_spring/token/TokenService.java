package dev.ngb.blog_spring.token;

import dev.ngb.blog_spring.user.User;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    String generateRefreshToken(User user, HttpServletRequest request);

    String generateVerifyToken(User user, HttpServletRequest request);

    void revokeAllUserTokens(User user);

    void revokeRefreshToken(String refreshToken);

    TokenInfo getTokenInfo(String token, TokenType tokenType);
}
