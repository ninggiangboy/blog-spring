package dev.ngb.blog_spring.storage;

import dev.ngb.blog_spring.exception.FileException;
import dev.ngb.blog_spring.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final S3Service s3Service;

    @Override
    public String store(User user, FileType type, MultipartFile file) {
        String fileExtension = checkInvalidFile(file, type);
        String userId = user.getId().toString().split("-")[0];
        String unique = UUID.randomUUID().toString().split("-")[0];
        String fileName = switch (type) {
            case IMAGE -> unique + '.' + fileExtension;
            case DOCUMENT -> unique + '/' + file.getOriginalFilename();
        };
        String filePath = String.join("/", type.getLocation(), userId, fileName);
        try {
            byte[] bytes = file.getBytes();
            s3Service.putFile(filePath, type.getType(), bytes);
        } catch (IOException e) {
            throw new FileException("Can't load file");
        }
        return filePath;
    }

    @Override
    public String getUrl(String filePath) {
        return s3Service.getFileUrl(filePath);
    }

    private String checkInvalidFile(MultipartFile file, FileType type) throws FileException {
        if (file == null || file.isEmpty()) {
            throw new FileException("Failed to storeFile empty file.");
        }
        String fileName = file.getName();
        if (fileName.isEmpty()) {
            throw new FileException("File has no valid name.");
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!type.getAllowedExtensions().contains(fileExtension)) {
            throw new FileException(
                    String.format("File extension %s is not allowed, only accept %s", fileExtension, type.getAllowedExtensions())
            );
        }

        if (file.getSize() > type.getMaxSize()) {
            throw new FileException("File must be <= " + type.getMaxSize() / 1_000_000L + "Mb");
        }
        return fileExtension;
    }
}
