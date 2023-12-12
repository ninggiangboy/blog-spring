package dev.ngb.blog_spring.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    <T> List<T> findAllByIsActiveIsTrueAndParentIsNull(Class<T> type);

    <T> Set<T> findAllByIsActiveIsTrueAndParent_Id(Class<T> type, Integer categoryId);

    boolean existsByNameLikeAndParentId(String name, Integer parentId);
}