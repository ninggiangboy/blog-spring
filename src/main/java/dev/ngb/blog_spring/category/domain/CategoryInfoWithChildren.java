package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link Category}
 */
@Data
public class CategoryInfoWithChildren implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Set<CategoryInfoWithChildren> children = new LinkedHashSet<>();
}