package dev.ngb.blog_spring.post.domain;

import dev.ngb.blog_spring.post.Post;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link Post}
 */
@Data
public class PostInfo implements Serializable {
    private Integer id;
    private AuthorInfo author;
    private String title;
    private String thumbnailImageUrl;
    private Post.Status status;
    private LocalDateTime publishedAt;
    private String slug;
    private Set<TagInfo> tags;
    private SeriesInfo series;
    private CategoryInfo category;

    /**
     * DTO for {@link dev.ngb.blog_spring.author.Author}
     */
    @Data
    public static class AuthorInfo implements Serializable {
        private Integer id;
        private String pseudonym;
    }

    /**
     * DTO for {@link dev.ngb.blog_spring.tag.Tag}
     */
    @Data
    public static class TagInfo implements Serializable {
        private Integer id;
        private String name;
    }

    /**
     * DTO for {@link dev.ngb.blog_spring.series.Series}
     */
    @Data
    public static class SeriesInfo implements Serializable {
        private Integer id;
        private String name;
    }

    /**
     * DTO for {@link dev.ngb.blog_spring.category.Category}
     */
    @Data
    public static class CategoryInfo implements Serializable {
        private Integer id;
        private String name;
    }
}