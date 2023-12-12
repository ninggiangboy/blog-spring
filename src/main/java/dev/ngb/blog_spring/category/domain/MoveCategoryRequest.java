package dev.ngb.blog_spring.category.domain;

import dev.ngb.blog_spring.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link Category}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MoveCategoryRequest implements Serializable {
    @NotNull(message = "Category id must be specified")
    private Integer id;
    private List<MoveCategoryRequest> children = new ArrayList<>();
}