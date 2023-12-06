package dev.ngb.blog.register;

import dev.ngb.blog.base.BaseController;
import dev.ngb.blog.base.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegisterController extends BaseController {

    private final RegisterService registerService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> register(
            @Validated @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request
    ) {
        registerService.register(registerRequest, request);
        return buildResponse("User registered successfully, please check your email to confirm");
    }

    @PutMapping("/confirm")
    public ResponseEntity<ResultResponse> confirmEmail(
            @RequestParam("token") String token,
            HttpServletRequest request
    ) {
        registerService.confirmEmail(token, request);
        return buildResponse("Email confirmed successfully");
    }
}
