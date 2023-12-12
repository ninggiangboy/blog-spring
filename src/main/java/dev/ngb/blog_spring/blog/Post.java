package dev.ngb.blog_spring.blog;

import dev.ngb.blog_spring.category.Category;
import dev.ngb.blog_spring.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts", schema = "blogs")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "slug", nullable = false, length = Integer.MAX_VALUE)
    private String slug;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "post_tag", schema = "blogs",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostVersion> postVersions = new LinkedHashSet<>();

}