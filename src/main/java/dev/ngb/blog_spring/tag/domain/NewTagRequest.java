package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@Value
public class NewTagRequest implements Serializable {
    @NotNull(message = "Tag name cannot be null")
    @Size(min = 2, max = 50, message = "Tag name must be between 1 and 50 characters")
    @NotBlank(message = "Tag name cannot be blank")
    String name;
    String description;
}