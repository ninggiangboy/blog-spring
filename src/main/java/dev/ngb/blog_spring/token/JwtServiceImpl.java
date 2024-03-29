package dev.ngb.blog_spring.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.blog_spring.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final String secretKey;
    private final long accessExpiration;
    private final ObjectMapper objectMapper;

    public JwtServiceImpl(TokenProperties tokenProperties, ObjectMapper objectMapper) {
        this.secretKey = tokenProperties.getSecretKey();
        this.accessExpiration = tokenProperties.getAccessExpiration();
        this.objectMapper = objectMapper;
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    @Override
    public Collection<SimpleGrantedAuthority> extractAuthorities(String token) {
        String aut = extractClaim(token, claims -> claims.get("aut", String.class));

        if (aut == null || aut.isEmpty()) {
            return Collections.emptySet();
        }
        try {
            byte[] decodedBytes = Decoders.BASE64.decode(aut);
            String decodedAuthorities = new String(decodedBytes, StandardCharsets.UTF_8);
            List<String> authorities = Arrays.asList(objectMapper.readValue(decodedAuthorities, String[].class));
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public String generateToken(User user, HttpServletRequest request) {
        return generateToken(getExtraClaims(user, request), user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, accessExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        long currentTime = System.currentTimeMillis();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, HttpServletRequest request) {
        return isTokenExpired(token) && isTokenFromIp(token, request) && isTokenFromUserAgent(token, request);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private boolean isTokenFromUserAgent(String token, HttpServletRequest request) {
        String ua = extractClaim(token, claims -> claims.get("ua", String.class));
        return ua.equalsIgnoreCase(request.getHeader("User-Agent"));
    }

    private boolean isTokenFromIp(String token, HttpServletRequest request) {
        String ip = extractClaim(token, claims -> claims.get("ip", String.class));
        return ip.equals(request.getRemoteAddr());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidBearerTokenException("Invalid JWT token: " + e.getMessage());
        }
    }

    private Map<String, Object> getExtraClaims(User user, HttpServletRequest request) {
        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Map<String, Object> extraClaims = new HashMap<>();
        try {
            String auth = objectMapper.writeValueAsString(authorities);
            String aut = Encoders.BASE64.encode(auth.getBytes());
            extraClaims.put("aut", aut);
            extraClaims.put("ip", request.getRemoteAddr());
            extraClaims.put("ua", request.getHeader("User-Agent"));
            extraClaims.put("email", user.getEmail());
            extraClaims.put("given_name", user.getFirstName());
            extraClaims.put("family_name", user.getLastName());
            extraClaims.put("username", user.getUsername());
            extraClaims.put("picture", user.getProfilePicture());
        } catch (JsonProcessingException e) {
            return extraClaims;
        }
        return extraClaims;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
