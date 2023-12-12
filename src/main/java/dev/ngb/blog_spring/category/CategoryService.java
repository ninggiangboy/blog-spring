package dev.ngb.blog_spring.category;

import dev.ngb.blog_spring.category.domain.CategoryInfoWithChildren;
import dev.ngb.blog_spring.category.domain.MoveCategoryRequest;
import dev.ngb.blog_spring.category.domain.NewCategoryRequest;
import dev.ngb.blog_spring.category.domain.UpdateCategoryRequest;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface CategoryService {
    List<CategoryInfoWithChildren> getAvailableCategories();

    Category createCategory(NewCategoryRequest newCategoryRequest);

    Category updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest);

    Category deleteCategory(Integer id);

    void moveCategories(List<MoveCategoryRequest> moveCategoryRequests) throws BadRequestException;
}
