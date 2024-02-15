package dev.ngb.blog_spring.post;

import dev.ngb.blog_spring.post.domain.PostProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
//    @Cacheable(value = "posts", key = "#page + ':' + #size + ':' + #sort")
    public Page<PostProjection> getAvailablePosts(Integer page, Integer size, String[] sort) {
        Pageable pageInfo = getPageable(page, size, sort);
        return postRepository.findByStatus(PostProjection.class, Post.Status.PUBLISHED, pageInfo);
    }

    private Sort getSort(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
            String[] split = s.split("\\.");
            if (split.length == 2) {
                Sort.Direction direction = Sort.Direction.fromString(split[1]);
                orders.add(new Sort.Order(direction, split[0]));
            } else {
                orders.add(new Sort.Order(Sort.Direction.DESC, split[0]));
            }
        }
        return Sort.by(orders);
    }

    private Pageable getPageable(Integer page, Integer size, String[] sort) {
        return PageRequest.of(page - 1, size, getSort(sort));
    }
}
