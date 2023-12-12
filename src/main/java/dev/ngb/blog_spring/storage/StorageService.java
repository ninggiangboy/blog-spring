package dev.ngb.blog_spring.storage;

import dev.ngb.blog_spring.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(User user, FileType type, MultipartFile file);

    String getUrl(String filePath);
}
