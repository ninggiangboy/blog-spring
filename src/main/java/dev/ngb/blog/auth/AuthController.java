package dev.ngb.blog.auth;

import dev.ngb.blog.base.BaseController;
import dev.ngb.blog.base.ResultResponse;
import dev.ngb.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResultResponse> authenticate(
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletRequest request
    ) {
        AuthResponse authResponse = authService.login(authRequest, request);
        return buildResponse("Login successfully", authResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResultResponse> refreshToken(
            @RequestParam("refresh_token") String refreshToken,
            HttpServletRequest request
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken, request);
        return buildResponse("Refresh token successfully", authResponse);
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResultResponse> logout(
            @RequestParam("refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        return buildResponse("Logout successfully");
    }

    @DeleteMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResultResponse> logoutAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAll(user);
        return buildResponse("Logout all devices successfully");
    }
}
