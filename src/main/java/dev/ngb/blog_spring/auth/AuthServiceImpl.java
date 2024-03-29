package dev.ngb.blog_spring.auth;

import dev.ngb.blog_spring.auth.domain.AuthRequest;
import dev.ngb.blog_spring.auth.domain.AuthResponse;
import dev.ngb.blog_spring.exception.NotFoundException;
import dev.ngb.blog_spring.token.JwtService;
import dev.ngb.blog_spring.token.TokenInfo;
import dev.ngb.blog_spring.token.TokenService;
import dev.ngb.blog_spring.token.TokenType;
import dev.ngb.blog_spring.user.User;
import dev.ngb.blog_spring.user.UserRepository;
import dev.ngb.blog_spring.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest authRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(user, request);
        String refreshToken = tokenService.generateRefreshToken(user, request);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken, HttpServletRequest request) {
        TokenInfo token = tokenService.getTokenInfo(refreshToken, TokenType.REFRESH);
        if (!IpUtil.isSameLocation(IpUtil.getIpAddress(request), token.getIpAddress())
                || isSameDevice(request.getHeader("User-Agent"), token.getUserAgent())
        ) {
            throw new AccessDeniedException("Your location is not allowed to refresh token");
        }
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        String accessToken = jwtService.generateToken(user, request);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean isSameDevice(String header, String userAgent) {
        return header.equalsIgnoreCase(userAgent);
    }

    @Override
    public void logout(String refreshToken, User user) {
        tokenService.revokeRefreshToken(refreshToken, user);
    }

    @Override
    public void logoutAll(User user) {
        tokenService.revokeAllUserTokens(user);
    }
}
