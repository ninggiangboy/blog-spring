package dev.ngb.blog_spring.category;

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
@Table(name = "categories", schema = "blogs")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "category_desc")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany(
            mappedBy = "parent",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<Category> children = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Post> posts = new LinkedHashSet<>();

}