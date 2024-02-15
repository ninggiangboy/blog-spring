package dev.ngb.blog_spring.series.domain;

import dev.ngb.blog_spring.series.Series;

/**
 * Projection for {@link Series}
 */
public interface SeriesProjection {
    Integer getId();

    String getName();

    String getDescription();
}