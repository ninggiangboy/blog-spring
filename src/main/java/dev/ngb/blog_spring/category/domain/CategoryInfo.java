package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Data
public class CategoryInfo implements Serializable {
    private Integer id;
    private String name;
    private String description;
}