package dev.ngb.blog.auth;

import dev.ngb.blog.exception.NotFoundException;
import dev.ngb.blog.token.TokenInfo;
import dev.ngb.blog.token.TokenService;
import dev.ngb.blog.token.TokenType;
import dev.ngb.blog.user.User;
import dev.ngb.blog.user.UserRepository;
import dev.ngb.blog.util.IpHelper;
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
        String jwtToken = jwtService.generateToken(user);
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
        if (!IpHelper.isSameLocation(IpHelper.getIpAddress(request), token.getIpAddress())) {
            throw new AccessDeniedException("Your location is not allowed to refresh token");
        }
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        String accessToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        tokenService.revokeRefreshToken(refreshToken);
    }

    @Override
    public void logoutAll(User user) {
        tokenService.revokeAllUserTokens(user);
    }
}
