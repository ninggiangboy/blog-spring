package dev.ngb.blog.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.ngb.blog.constant.ValidationRegex;
import dev.ngb.blog.user.User;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class RegisterRequest implements Serializable {
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @NotBlank(message = "Username cannot be blank")
    String username;
    @NotNull(message = "Email cannot be null")
    @Size(min = 3, max = 100, message = "Email must be between 3 and 100 characters")
    @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
    @NotBlank(message = "Email cannot be blank")
    String email;
    @NotNull(message = "First name cannot be null")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    @NotBlank(message = "First name cannot be blank")
    @JsonProperty("first_name")
    String firstName;
    @NotNull(message = "Last name cannot be null")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    @NotBlank(message = "Last name cannot be blank")
    @JsonProperty("last_name")
    String lastName;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ValidationRegex.PASSWORD_REGEX, message = "The password must have at least one uppercase letter, one lowercase letter, and one numeric digit")
    @NotBlank(message = "Password cannot be blank")
    String password;
}