select p1_0.tags_tag_id,
       p1_1.post_id,
       p1_1.author_id,
       p1_1.category_id,
       p1_1.post_title,
       p1_1.published_at,
       p1_1.series_id,
       p1_1.slug,
       p1_1.post_status,
       p1_1.thumbnail_image_url
from blogs.post_tag p1_0
         join blogs.posts p1_1 on p1_1.post_id = p1_0.posts_post_id
where p1_0.tags_tag_id=?