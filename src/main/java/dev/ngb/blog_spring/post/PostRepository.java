package dev.ngb.blog_spring.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @EntityGraph(attributePaths = {"author", "tags", "series", "category"})
    <T> Page<T> findByStatus(Class<T> type, Post.Status status, Pageable pageable);
}