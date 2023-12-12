package dev.ngb.blog_spring.tag;

import dev.ngb.blog_spring.tag.domain.NewTagRequest;
import dev.ngb.blog_spring.tag.domain.TagInfo;
import dev.ngb.blog_spring.tag.domain.UpdateTagRequest;

import java.util.List;

public interface TagService {
    List<TagInfo> getAvailableTags();

    Tag createTag(NewTagRequest newNewTagRequest);

    Tag updateTag(Integer id, UpdateTagRequest updateNewTagRequest);

    Tag deleteTag(Integer id, boolean removeFromPosts);
}
