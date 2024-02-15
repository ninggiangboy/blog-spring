package dev.ngb.blog_spring.post.domain;

import dev.ngb.blog_spring.post.Post;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Projection for {@link Post}
 */
public interface PostProjection {
    Integer getId();

    String getTitle();

    String getThumbnailImageUrl();

    LocalDateTime getPublishedAt();

    String getSlug();

    AuthorProjection getAuthor();

    Set<TagProjection> getTags();

    SeriesProjection getSeries();

    CategoryProjection getCategory();

    /**
     * Projection for {@link dev.ngb.blog_spring.author.Author}
     */
    interface AuthorProjection {
        Integer getId();

        String getPseudonym();
    }

    /**
     * Projection for {@link dev.ngb.blog_spring.tag.Tag}
     */
    interface TagProjection {
        Integer getId();

        String getName();
    }

    /**
     * Projection for {@link dev.ngb.blog_spring.series.Series}
     */
    interface SeriesProjection {
        Integer getId();

        String getName();
    }

    /**
     * Projection for {@link dev.ngb.blog_spring.category.Category}
     */
    interface CategoryProjection {
        Integer getId();

        String getName();
    }
}