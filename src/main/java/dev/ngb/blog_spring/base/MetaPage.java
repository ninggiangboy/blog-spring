package dev.ngb.blog_spring.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MetaPage {
    Integer page;
    @JsonProperty("per_page")
    Integer perPage;
    @JsonProperty("total_elements")
    Long totalElements;
    @JsonProperty("total_pages")
    Integer totalPages;
    @JsonProperty("has_prev")
    Boolean hasPrev;
    @JsonProperty("has_next")
    Boolean hasNext;
}
