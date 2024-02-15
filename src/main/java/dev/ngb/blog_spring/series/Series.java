package dev.ngb.blog_spring.series;

import dev.ngb.blog_spring.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "series", schema = "blogs")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id")
    private Integer id;

    @Column(name = "series_name")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "series_desc")
    private String description;

    @OneToMany(mappedBy = "series")
    private Set<Post> posts = new LinkedHashSet<>();

}