package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;
import dev.ngb.blog_spring.validation.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateTagRequest implements Serializable {
    @Size(min = 2, max = 50, message = "Tag name must be between 1 and 50 characters")
    @NullOrNotBlank(message = "Tag name cannot be blank")
    String name;
    String description;
}