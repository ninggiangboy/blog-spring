package dev.ngb.blog.register;

import dev.ngb.blog.base.BaseController;
import dev.ngb.blog.base.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class RegisterController extends BaseController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<ResultResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request
    ) {
        registerService.register(registerRequest, request);
        return buildResponse("User registered successfully, please check your email to confirm");
    }

    @PutMapping("/register/{token}")
    public ResponseEntity<ResultResponse> confirmEmail(
            @PathVariable("token") String token,
            HttpServletRequest request
    ) {
        registerService.confirmEmail(token, request);
        return buildResponse("Email confirmed successfully");
    }
}
