package dev.ngb.blog.register;

import jakarta.servlet.http.HttpServletRequest;

public interface RegisterService {
    void register(RegisterRequest registerRequest, HttpServletRequest request);

    void confirmEmail(String token, HttpServletRequest request);
}
