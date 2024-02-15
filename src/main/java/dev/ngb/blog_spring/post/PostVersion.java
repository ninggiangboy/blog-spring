package dev.ngb.blog_spring.post;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post_versions", schema = "blogs")
public class PostVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_versions_id_gen")
    @SequenceGenerator(name = "post_versions_id_gen", sequenceName = "post_versions_post_versions_id_seq", allocationSize = 1)
    @Column(name = "post_versions_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

}