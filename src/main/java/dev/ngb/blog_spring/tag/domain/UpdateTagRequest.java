package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;
import dev.ngb.blog_spring.validation.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@Value
public class UpdateTagRequest implements Serializable {
    @Size(min = 2, max = 50, message = "Tag name must be between 1 and 50 characters")
    @NullOrNotBlank(message = "Tag name cannot be blank")
    String name;
    String description;
}