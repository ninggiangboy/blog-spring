package dev.ngb.blog.validation;

import dev.ngb.blog.constant.ValidationRegex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        return email.matches(ValidationRegex.EMAIL_REGEX);
    }
}
