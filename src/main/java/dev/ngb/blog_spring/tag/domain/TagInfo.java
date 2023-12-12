package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;
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
public class TagInfo implements Serializable {
    private Integer id;
    private String name;
    private String description;
}