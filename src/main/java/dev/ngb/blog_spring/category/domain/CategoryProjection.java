package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;

/**
 * Projection for {@link Category}
 */
public interface CategoryProjection {
    Integer getId();

    String getName();

    String getDescription();
}