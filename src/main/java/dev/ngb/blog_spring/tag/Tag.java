package dev.ngb.blog_spring.tag;

import dev.ngb.blog_spring.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tags", schema = "blogs")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer id;

    @Column(name = "tag_name")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "tag_desc")
    private String description;

    @ManyToMany(
            mappedBy = "tags",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            }
    )
    private Set<Post> posts = new HashSet<>();
}