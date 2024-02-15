package dev.ngb.blog_spring.category;

import dev.ngb.blog_spring.category.domain.*;
import dev.ngb.blog_spring.exception.DuplicateException;
import dev.ngb.blog_spring.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Override
//    @Cacheable("categories")
    public List<CategoryInfoWithChildren> getAvailableCategories() {
        List<CategoryInfoWithChildren> roots = categoryRepository
                .findAllByIsActiveIsTrueAndParentIsNull(CategoryProjection.class)
                .stream().map(element -> mapper.map(element, CategoryInfoWithChildren.class)).toList();
        roots.forEach(this::fillChildren);
        return roots;
    }

    private void fillChildren(CategoryInfoWithChildren category) {
        Set<CategoryInfoWithChildren> children = categoryRepository.findAllByIsActiveIsTrueAndParent_Id(CategoryProjection.class, category.getId())
                .stream().map(element -> mapper.map(element, CategoryInfoWithChildren.class)).collect(Collectors.toSet());
        if (!children.isEmpty()) {
            category.setChildren(children);
            children.forEach(this::fillChildren);
        }
    }

    @Override
    @Transactional
    public Category createCategory(NewCategoryRequest newCategoryRequest) {
        if (categoryRepository.existsByNameLikeAndParentId(newCategoryRequest.getName(), newCategoryRequest.getParentId())) {
            throw new DuplicateException(
                    String.format("Category with name %s already exists in same level (parent)", newCategoryRequest.getName())
            );
        }
        Category newCategory = mapper.map(newCategoryRequest, Category.class);
        if (newCategoryRequest.getParentId() != null) {
            Category parentCategory = getCategoryById(newCategoryRequest.getParentId());
            newCategory.setParent(parentCategory);
        }

        return categoryRepository.save(newCategory);
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = getCategoryById(id);
        if (categoryRepository.existsByNameLikeAndParentId(updateCategoryRequest.getName(), category.getParent().getId())
                && !category.getName().equals(updateCategoryRequest.getName())
        ) {
            throw new DuplicateException(
                    String.format("Category with name %s already exists in same level (parent)", updateCategoryRequest.getName())
            );
        }
        mapper.map(updateCategoryRequest, category);
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategory(Integer id) {
        Category category = getCategoryById(id);
        category.setIsActive(false);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void moveCategories(List<MoveCategoryRequest> moveCategoryRequests) throws BadRequestException {
        validateMoveCategoryRequest(moveCategoryRequests);
        moveCategoryRequests.forEach(moveCategoryRequest -> {
            Category sourceCategory = getCategoryById(moveCategoryRequest.getId());
            updateParentRelationships(sourceCategory, moveCategoryRequest.getChildren());
            sourceCategory.setParent(null);
            categoryRepository.save(sourceCategory);
        });
    }

    private void validateMoveCategoryRequest(List<MoveCategoryRequest> moveCategoryRequests) throws BadRequestException {
        List<Integer> ids = getAllIds(moveCategoryRequests);
        if (ids.size() != ids.stream().distinct().count()) {
            throw new DuplicateException("Duplicate ids in request");
        }
        if (ids.size() != categoryRepository.count()) {
            throw new BadRequestException("Invalid categories request");
        }
        Set<Integer> allIds = categoryRepository.findAllBy(CategoryIdProjection.class).stream().map(CategoryIdProjection::getId).collect(Collectors.toSet());
        if (!allIds.containsAll(ids)) {
            throw new BadRequestException("Invalid category in request");
        }
    }

    private void updateParentRelationships(Category parentCategory, List<MoveCategoryRequest> children) {
        children.forEach(child -> {
            Category childCategory = getCategoryById(child.getId());
            childCategory.setParent(parentCategory);
            categoryRepository.save(childCategory);
            updateParentRelationships(childCategory, child.getChildren());
        });
    }

    private List<Integer> getAllIds(List<MoveCategoryRequest> categories) {
        List<Integer> ids = new ArrayList<>();
        categories.forEach(
                category -> {
                    ids.add(category.getId());
                    ids.addAll(getAllIds(category.getChildren()));
                }
        );
        return ids;
    }

    private Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
    }
}
