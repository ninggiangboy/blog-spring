package dev.ngb.blog_spring.token;

import dev.ngb.blog_spring.exception.ExpiredException;
import dev.ngb.blog_spring.user.User;
import dev.ngb.blog_spring.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String KEY_PATTERN = "%s::%s::%s";
    private static final String VALUE_PATTERN = "%s::%s";
    private final long refreshExpiration;
    private final long verifiedExpiration;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenServiceImpl(TokenProperties tokenProperties, RedisTemplate<String, String> redisTemplate) {
        this.refreshExpiration = tokenProperties.getRefreshExpiration();
        this.verifiedExpiration = tokenProperties.getVerifiedExpiration();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public TokenInfo getTokenInfo(String token, TokenType tokenType) {
        String keyPattern = String.format(KEY_PATTERN, tokenType.name(), "*", token);
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            throw new ExpiredException("Invalid or expired token");
        }
        String key = keys.iterator().next();
        String[] value = redisTemplate.opsForValue().get(key).split("::");
        return TokenInfo.builder()
                .userId(UUID.fromString(key.split("::")[1]))
                .ipAddress(value[0])
                .userAgent(value[1])
                .build();
    }

    @Override
    public String generateRefreshToken(User user, HttpServletRequest request) {
        return saveUserToken(user, request, TokenType.REFRESH, refreshExpiration);
    }

    @Override
    public String generateVerifyToken(User user, HttpServletRequest request) {
        return saveUserToken(user, request, TokenType.VERIFIED, verifiedExpiration);
    }

    private String saveUserToken(User user, HttpServletRequest request, TokenType tokenType, long expiration) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                String.format(KEY_PATTERN, tokenType.name(), user.getId().toString(), token),
                String.format(VALUE_PATTERN, IpUtil.getIpAddress(request), request.getHeader("User-Agent")),
                expiration, TimeUnit.MILLISECONDS
        );
        return token;
    }

    @Override
    public void revokeAllUserTokens(User user) {
        String keyPattern = String.format(KEY_PATTERN, TokenType.REFRESH.name(), user.getId().toString(), "*");
        Set<String> refreshTokens = redisTemplate.keys(keyPattern);
        if (refreshTokens != null && !refreshTokens.isEmpty()) {
            refreshTokens.forEach(redisTemplate::delete);
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken, User user) throws ExpiredException {
        UUID userId = getTokenInfo(refreshToken, TokenType.REFRESH).getUserId();
        if (!userId.equals(user.getId())) {
            throw new ExpiredException("Invalid refresh token");
        }
        redisTemplate.delete(String.format(KEY_PATTERN, TokenType.REFRESH.name(), userId, refreshToken));
    }
}
