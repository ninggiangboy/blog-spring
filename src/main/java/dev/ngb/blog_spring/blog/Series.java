package dev.ngb.blog_spring.blog;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_id_gen")
    @SequenceGenerator(name = "series_id_gen", sequenceName = "series_series_id_seq", allocationSize = 1)
    @Column(name = "series_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "series_name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "series_desc", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "series")
    private Set<Post> posts = new LinkedHashSet<>();

}