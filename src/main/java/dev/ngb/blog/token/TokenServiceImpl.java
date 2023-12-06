package dev.ngb.blog.token;

import dev.ngb.blog.exception.ExpiredException;
import dev.ngb.blog.user.User;
import dev.ngb.blog.util.IpHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String USER_PREFIX = "USER_";
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
        String key = tokenType.getRedisPrefix() + token;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new ExpiredException("Invalid or expired token");
        }
        String[] parts = value.split(" : ");
        return TokenInfo.builder()
                .userId(UUID.fromString(parts[0]))
                .ipAddress(parts[1])
                .build();
    }

    @Override
    public String generateRefreshToken(User user, HttpServletRequest request) {
        String refreshToken = saveUserToken(user, request, TokenType.REFRESH, refreshExpiration);
        String userKey = USER_PREFIX + TokenType.REFRESH.getRedisPrefix() + user.getId();
        redisTemplate.opsForList().rightPush(userKey, refreshToken);
        return refreshToken;
    }

    @Override
    public String generateVerifyToken(User user, HttpServletRequest request) {
        return saveUserToken(user, request, TokenType.VERIFIED, verifiedExpiration);
    }

    private String saveUserToken(User user, HttpServletRequest request, TokenType tokenType, long expiration) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                tokenType.getRedisPrefix() + token,
                user.getId() + " : " + IpHelper.getIpAddress(request),
                expiration, TimeUnit.MILLISECONDS
        );
        return token;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // Execute every day
    public void removeExpiredTokens() {
        Set<String> userKeys = redisTemplate.keys(USER_PREFIX + TokenType.REFRESH.getRedisPrefix() + "*");
        if (userKeys != null) {
            userKeys.forEach(this::removeExpiredTokensForUser);
        }
    }

    private void removeExpiredTokensForUser(String userKey) {
        List<String> refreshTokens = redisTemplate.opsForList().range(userKey, 0, -1);
        if (refreshTokens != null) {
            refreshTokens.forEach(refreshToken -> {
                if (Boolean.FALSE.equals(redisTemplate.hasKey(TokenType.REFRESH.getRedisPrefix() + refreshToken))) {
                    redisTemplate.opsForList().remove(userKey, 0, refreshToken);
                }
            });
        }
    }

    @Override
    public void revokeAllUserTokens(User user) {
        String userKey = USER_PREFIX + TokenType.REFRESH.getRedisPrefix() + user.getId();
        List<String> refreshTokens = redisTemplate.opsForList().range(userKey, 0, -1);
        if (refreshTokens != null) {
            refreshTokens.forEach(key -> redisTemplate.delete(TokenType.REFRESH.getRedisPrefix() + key));
        }
        redisTemplate.delete(userKey);
    }

    @Override
    public void revokeRefreshToken(String refreshToken) throws ExpiredException {
        UUID userId = getTokenInfo(refreshToken, TokenType.REFRESH).getUserId();
        redisTemplate.delete(TokenType.REFRESH.getRedisPrefix() + refreshToken);
        String userKey = USER_PREFIX + TokenType.REFRESH.getRedisPrefix() + userId;
        redisTemplate.opsForList().remove(userKey, 0, refreshToken);
    }
}
