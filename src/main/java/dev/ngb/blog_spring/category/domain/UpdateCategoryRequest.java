package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import dev.ngb.blog_spring.validation.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class UpdateCategoryRequest implements Serializable {
    @Size(min = 2, max = 50, message = "Category name must be between 1 and 50 characters")
    @NullOrNotBlank(message = "Category name cannot be blank")
    String name;
    String description;
}