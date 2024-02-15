package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@Data
public class TagInfo implements Serializable {
    private Integer id;
    private String name;
    private String description;
}