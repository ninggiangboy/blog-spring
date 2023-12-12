package dev.ngb.blog_spring.register;

import dev.ngb.blog_spring.email.EmailService;
import dev.ngb.blog_spring.exception.NotFoundException;
import dev.ngb.blog_spring.token.TokenInfo;
import dev.ngb.blog_spring.token.TokenService;
import dev.ngb.blog_spring.token.TokenType;
import dev.ngb.blog_spring.user.User;
import dev.ngb.blog_spring.user.UserRepository;
import dev.ngb.blog_spring.user.UserService;
import dev.ngb.blog_spring.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest, HttpServletRequest request) {
        User user = userService.createUser(registerRequest);
        String confirmToken = tokenService.generateVerifyToken(user, request);
        sendVerificationEmail(user, confirmToken);
    }

    private void sendVerificationEmail(User user, String confirmToken) {
        String subject = "Confirm Your Email Address";
        Context context = new Context();
        context.setVariable("token", confirmToken);
        String html = templateEngine.process("email/email-confirmation.html", context);
        emailService.sendEmail(subject, html, user.getEmail(), true);
    }

    @Override
    public void confirmEmail(String confirmToken, HttpServletRequest request) {
        TokenInfo token = tokenService.getTokenInfo(confirmToken, TokenType.VERIFIED);
        if (!IpUtil.isSameLocation(IpUtil.getIpAddress(request), token.getIpAddress())) {
            throw new AccessDeniedException("Your location is not allowed to refresh token");
        }
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        user.setIsVerified(true);
        userRepository.save(user);
        tokenService.revokeAllUserTokens(user);
    }
}
