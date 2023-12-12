package dev.ngb.blog_spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(String content, ConstraintValidatorContext context) {
        return content == null || !content.trim().isBlank();
    }
}
