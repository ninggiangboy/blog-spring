package dev.ngb.blog_spring.category.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.ngb.blog_spring.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class NewCategoryRequest implements Serializable {
    @NotNull(message = "Category name cannot be null")
    @Size(min = 2, max = 50, message = "Category name must be between 1 and 50 characters")
    @NotBlank(message = "Category name cannot be blank")
    String name;
    String description;
    @JsonProperty("parent_id")
    Integer parentId;
}