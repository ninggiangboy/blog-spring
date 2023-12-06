package dev.ngb.blog.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permissions", schema = "blogs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id", nullable = false)
    private Integer id;

    @Column(name = "permission_code", nullable = false, length = 30)
    private String code;
    
//    @Column(name = "permission_name", nullable = false, length = 50)
//    private String name;
//
//    @Column(name = "permission_desc", length = Integer.MAX_VALUE)
//    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private final Set<Role> roles = new LinkedHashSet<>();
}