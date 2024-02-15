package dev.ngb.blog_spring.category;

import dev.ngb.blog_spring.base.BaseController;
import dev.ngb.blog_spring.base.ResultResponse;
import dev.ngb.blog_spring.category.domain.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper mapper;

    @GetMapping("/categories")
    public ResponseEntity<ResultResponse> getAllCategories() {
        List<CategoryInfoWithChildren> categories = categoryService.getAvailableCategories();
        return buildResponse("Found all available categories", categories);
    }

    @PutMapping("/categories")
    public ResponseEntity<ResultResponse> moveCategories(
            @Valid @RequestBody List<MoveCategoryRequest> moveCategoryRequests
    ) throws BadRequestException {
        categoryService.moveCategories(moveCategoryRequests);
        List<CategoryInfoWithChildren> categories = categoryService.getAvailableCategories();
        return buildResponse("Moved categories", categories);
    }

    @PostMapping("/category")
    public ResponseEntity<ResultResponse> createCategory(@Valid @RequestBody NewCategoryRequest newCategoryRequest) {
        Category createdCategory = categoryService.createCategory(newCategoryRequest);
        CategoryIdProjection categoryInfo = mapper.map(createdCategory, CategoryIdProjection.class);
        return buildResponse("Created category", categoryInfo);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<ResultResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        Category updatedCategory = categoryService.updateCategory(id, updateCategoryRequest);
        CategoryIdProjection categoryInfo = mapper.map(updatedCategory, CategoryIdProjection.class);
        return buildResponse("Updated category", categoryInfo);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<ResultResponse> deleteCategory(@PathVariable Integer id) {
        Category deletedCategory = categoryService.deleteCategory(id);
        CategoryIdProjection categoryInfo = mapper.map(deletedCategory, CategoryIdProjection.class);
        return buildResponse("Deleted category", categoryInfo);
    }
}
