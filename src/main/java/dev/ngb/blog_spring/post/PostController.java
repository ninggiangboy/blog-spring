package dev.ngb.blog_spring.post;

import dev.ngb.blog_spring.base.BaseController;
import dev.ngb.blog_spring.base.ResultResponse;
import dev.ngb.blog_spring.post.domain.PostProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<ResultResponse> getPosts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "per_page", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "{publishedAt:desc}") String[] sort
    ) {
        Page<PostProjection> pagePosts = postService.getAvailablePosts(page, size, sort);
        return buildResponse("Posts", pagePosts);
    }

//    @GetMapping("/posts/{slug}")
//    public ResponseEntity<ResultResponse> getPostBySlug(@RequestParam(value = "slug") String slug) {
//        PostProjection post = postService.getPostBySlug(slug);
//        return buildResponse("Post", post);
//    }
}
