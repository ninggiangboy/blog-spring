package dev.ngb.blog.user;

import dev.ngb.blog.register.RegisterRequest;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
}
