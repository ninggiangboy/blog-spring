package dev.ngb.blog_spring.series;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Integer> {
    <T> List<T> findByIsActiveTrue(Class<T> type);
}