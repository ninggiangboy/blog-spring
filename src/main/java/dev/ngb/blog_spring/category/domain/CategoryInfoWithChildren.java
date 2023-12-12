package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link Category}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryInfoWithChildren implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Set<CategoryInfoWithChildren> children = new LinkedHashSet<>();
}