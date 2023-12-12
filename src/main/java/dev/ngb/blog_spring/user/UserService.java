package dev.ngb.blog_spring.user;

import dev.ngb.blog_spring.register.RegisterRequest;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
}
