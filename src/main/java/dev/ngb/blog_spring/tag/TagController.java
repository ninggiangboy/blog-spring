package dev.ngb.blog_spring.tag;

import dev.ngb.blog_spring.base.BaseController;
import dev.ngb.blog_spring.base.ResultResponse;
import dev.ngb.blog_spring.tag.domain.NewTagRequest;
import dev.ngb.blog_spring.tag.domain.TagInfo;
import dev.ngb.blog_spring.tag.domain.UpdateTagRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class TagController extends BaseController {

    private final TagService tagService;
    private final ModelMapper mapper;

    @GetMapping("/tags")
    public ResponseEntity<ResultResponse> getTags() {
        List<TagInfo> tags = tagService.getAvailableTags();
        return buildResponse("Found all available tags", tags);
    }

    @PostMapping("/tag")
    public ResponseEntity<ResultResponse> createTag(@RequestBody NewTagRequest newNewTagRequest) {
        Tag createdTag = tagService.createTag(newNewTagRequest);
        TagInfo tagInfo = mapper.map(createdTag, TagInfo.class);
        return buildResponse("Created new tag", tagInfo);
    }

    @PutMapping("/tag/{id}")
    public ResponseEntity<ResultResponse> updateTag(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTagRequest updateTagRequest
    ) {
        Tag updatedTag = tagService.updateTag(id, updateTagRequest);
        TagInfo tagInfo = mapper.map(updatedTag, TagInfo.class);
        return buildResponse("Updated tag", tagInfo);
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<ResultResponse> deleteTag(
            @PathVariable Integer id,
            @RequestParam(
                    name = "remove_from_posts",
                    required = false,
                    defaultValue = "true"
            ) boolean removeFromPosts
    ) {
        Tag deleteTag = tagService.deleteTag(id, removeFromPosts);
        TagInfo tagInfo = mapper.map(deleteTag, TagInfo.class);
        return buildResponse("Deactivate tag", tagInfo);
    }
}
