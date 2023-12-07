package dev.ngb.blog.token;

import dev.ngb.blog.exception.ExpiredException;
import dev.ngb.blog.user.User;
import dev.ngb.blog.util.IpHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String KEY_PATTERN = "%s:%s:%s";
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
        String keyPattern = String.join(":", tokenType.name(), "*", token);
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            throw new ExpiredException("Invalid or expired token");
        }
        String key = keys.iterator().next();
        String ipAddress = redisTemplate.opsForValue().get(key);
        return TokenInfo.builder()
                .userId(UUID.fromString(key.split(":")[1]))
                .ipAddress(ipAddress)
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
                String.format(KEY_PATTERN, tokenType, user.getId().toString(), token),
                IpHelper.getIpAddress(request),
                expiration, TimeUnit.MILLISECONDS
        );
        return token;
    }

    @Override
    public void revokeAllUserTokens(User user) {
        String keyPattern = String.format(KEY_PATTERN, TokenType.REFRESH, user.getId().toString(), "*");
        Set<String> refreshTokens = redisTemplate.keys(keyPattern);
        if (refreshTokens != null && !refreshTokens.isEmpty()) {
            refreshTokens.forEach(key -> {
                if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                    redisTemplate.delete(key);
                }
            });
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken) throws ExpiredException {
        UUID userId = getTokenInfo(refreshToken, TokenType.REFRESH).getUserId();
        redisTemplate.delete(String.format(KEY_PATTERN, TokenType.REFRESH, userId.toString(), refreshToken));
    }
}
