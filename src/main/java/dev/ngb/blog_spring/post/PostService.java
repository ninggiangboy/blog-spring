package dev.ngb.blog_spring.post;

import dev.ngb.blog_spring.post.domain.PostProjection;
import org.springframework.data.domain.Page;

public interface PostService {
    Page<PostProjection> getAvailablePosts(Integer page, Integer size, String[] sort);
}
