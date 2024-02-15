select p1_0.post_id,
       a1_0.author_id,
       a1_0.author_pseudonym,
       a1_0.user_id,
       c1_0.category_id,
       c1_0.category_name,
       c1_0.parent_category_id,
       p1_0.published_at,
       s1_0.series_id,
       s1_0.series_name,
       p1_0.slug,
       p1_0.post_status,
       t1_1.tag_id,
       t1_1.tag_name,
       p1_0.thumbnail_image_url,
       p1_0.post_title
from blogs.posts p1_0
         left join
     blogs.authors a1_0
     on a1_0.author_id = p1_0.author_id
         left join
     blogs.categories c1_0
     on c1_0.category_id = p1_0.category_id
         left join
     blogs.series s1_0
     on s1_0.series_id = p1_0.series_id
         left join
     (blogs.post_tag t1_0
         join
         blogs.tags t1_1
      on t1_1.tag_id = t1_0.tag_id)
     on p1_0.post_id = t1_0.post_id
where p1_0.post_status = 'PUBLISHED'