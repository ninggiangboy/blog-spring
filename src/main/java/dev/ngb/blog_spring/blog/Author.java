package dev.ngb.blog_spring.blog;

import dev.ngb.blog_spring.user.User;
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
@Table(name = "authors", schema = "blogs")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer id;

    @Column(name = "author_pseudonym")
    private String pseudonym;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "author_desc")
    private String description;

    @OneToMany(mappedBy = "author")
    private Set<Post> posts = new LinkedHashSet<>();
}