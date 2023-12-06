package dev.ngb.blog.user;

import dev.ngb.blog.exception.DuplicateException;
import dev.ngb.blog.register.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String username = registerRequest.getUsername();
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new DuplicateException("Email or Username already exists");
        }
        User user = mapUser(registerRequest);
        user.getRoles().add(roleRepository
                .findByCode("USER")
                .orElse(roleRepository.save(Role.builder().code("USER").build()))
        );
        return userRepository.save(user);
    }

    private User mapUser(RegisterRequest registration) {
        User user = mapper.map(registration, User.class);
        user.setPassword(encoder.encode(registration.getPassword()));
        user.setIsVerified(false);
        user.setIsBlocked(false);
        return user;
    }

}
