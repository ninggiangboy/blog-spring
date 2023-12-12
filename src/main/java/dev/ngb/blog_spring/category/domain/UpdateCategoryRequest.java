package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import dev.ngb.blog_spring.validation.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateCategoryRequest implements Serializable {
    @Size(min = 2, max = 50, message = "Category name must be between 1 and 50 characters")
    @NullOrNotBlank(message = "Category name cannot be blank")
    private String name;
    private String description;
}