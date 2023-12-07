package dev.ngb.blog.storage;

import dev.ngb.blog.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(User user, FileType type, MultipartFile file);

    String getUrl(String filePath);
}
