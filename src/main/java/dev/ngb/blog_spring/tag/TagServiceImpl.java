package dev.ngb.blog_spring.tag;

import dev.ngb.blog_spring.exception.DuplicateException;
import dev.ngb.blog_spring.exception.NotFoundException;
import dev.ngb.blog_spring.tag.domain.NewTagRequest;
import dev.ngb.blog_spring.tag.domain.TagInfo;
import dev.ngb.blog_spring.tag.domain.TagProjection;
import dev.ngb.blog_spring.tag.domain.UpdateTagRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private static final String TAG_KEY = "tags";
    private final TagRepository tagRepository;
    private final ModelMapper mapper;

//    private final RedisTemplate<String, Object> redisTemplate;
//    private final ObjectMapper objectMapper;

//    @Override
//    public List<TagInfo> getAvailableTags() throws JsonProcessingException {
//        String redisData = (String) redisTemplate.opsForValue().get(TAG_KEY + "::all");
//        if (redisData != null && !redisData.isEmpty()) {
//            return List.of(objectMapper.readValue(redisData, TagInfo[].class));
//        }
//        List<TagInfo> tags = tagRepository.findAllByIsActiveIsTrue(TagProjection.class).stream()
//                .map(tagProjection -> mapper.map(tagProjection, TagInfo.class))
//                .toList();
//        redisTemplate.opsForValue().set(TAG_KEY + "::all", objectMapper.writeValueAsString(tags));
//        return tags;
//    }

    @Override
    public List<TagInfo> getAvailableTags() {
        return tagRepository.findAllByIsActiveIsTrue(TagProjection.class)
                .stream()
                .map(tagProjection -> mapper.map(tagProjection, TagInfo.class))
                .toList();
    }

    @Override
    @Transactional
    public Tag createTag(NewTagRequest newNewTagRequest) {
        Tag newTag = tagRepository
                .findByName(newNewTagRequest.getName())
                .map(tag -> {
                    tag.setDescription(newNewTagRequest.getDescription());
                    return tag;
                })
                .orElse(mapper.map(newNewTagRequest, Tag.class));
        newTag.setIsActive(true);
        return tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public Tag updateTag(Integer id, UpdateTagRequest updateNewTagRequest) {
        Tag tag = getTagById(id);
        if (tagRepository.existsByName(updateNewTagRequest.getName())
                && !tag.getName().equals(updateNewTagRequest.getName())
        ) {
            throw new DuplicateException(String.format("Tag with name %s already exists", updateNewTagRequest.getName()));
        }
        mapper.map(updateNewTagRequest, tag);
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag deleteTag(Integer id, boolean removeFromPosts) {
        Tag tag = getTagById(id);
        if (removeFromPosts) {
            tag.setPosts(new HashSet<>());
        }
        tag.setIsActive(false);
        return tagRepository.save(tag);
    }

    private Tag getTagById(Integer id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Tag with id %s not found", id))
        );
    }
}
