package dev.ngb.blog_spring.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);

    <T> List<T> findAllByIsActiveIsTrue(Class<T> type);

    Optional<Tag> findByName(String name);
}