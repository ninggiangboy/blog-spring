package dev.ngb.blog_spring.tag.domain;

import dev.ngb.blog_spring.tag.Tag;

/**
 * Projection for {@link Tag}
 */
public interface TagProjection {
    Integer getId();

    String getName();

    String getDescription();
}