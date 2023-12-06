select r1_0.user_id, r1_1.role_id, r1_1.role_code, r1_1.role_desc, r1_1.role_name
from blogs.user_roles r1_0
         join blogs.roles r1_1 on r1_1.role_id = r1_0.role_id
where r1_0.user_id = 'da1f7638-b697-4f15-a9a7-d0768c5522e0'