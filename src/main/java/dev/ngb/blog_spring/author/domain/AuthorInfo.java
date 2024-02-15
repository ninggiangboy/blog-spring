package dev.ngb.blog_spring.author.domain;

import dev.ngb.blog_spring.author.Author;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Author}
 */
@Data
public class AuthorInfo implements Serializable {
    private Integer id;
    private String pseudonym;
    private String description;
}
