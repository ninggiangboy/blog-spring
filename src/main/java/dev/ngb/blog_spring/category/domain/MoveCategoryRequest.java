package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for {@link Category}
 */
@Value
public class MoveCategoryRequest {
    @NotNull(message = "Category id must be specified")
    Integer id;
    List<MoveCategoryRequest> children;
}