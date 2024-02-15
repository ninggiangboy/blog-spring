package dev.ngb.blog_spring.series.domain;

import dev.ngb.blog_spring.series.Series;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Series}
 */
@Data
public class SeriesInfo implements Serializable {
    private Integer id;
    private String name;
    private String description;
}