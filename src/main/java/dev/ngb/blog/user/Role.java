package dev.ngb.blog.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "blogs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "role_permission",
            schema = "blogs",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private final Set<Permission> permissions = new LinkedHashSet<>();
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private final Set<User> users = new LinkedHashSet<>();
    //    @Column(name = "role_name", nullable = false, length = 50)
//    private String name;
//
//    @Column(name = "role_desc", length = Integer.MAX_VALUE)
//    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;
    @Column(name = "role_code", nullable = false, length = 10)
    private String code;

    public Stream<SimpleGrantedAuthority> getAuthorities() {
        Stream<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()));
        return Stream.concat(authorities, Stream.of(new SimpleGrantedAuthority("ROLE_" + code)));
    }
}