package dev.ngb.blog.storage;

import dev.ngb.blog.base.BaseController;
import dev.ngb.blog.base.ResultResponse;
import dev.ngb.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class StorageController extends BaseController {
    private final StorageService storageService;

    @PostMapping("/image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResultResponse> uploadImage(
            @RequestParam MultipartFile file,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        String filePath = storageService.store(user, FileType.IMAGE, file);
        String fileUrl = storageService.getUrl(filePath);
        return buildResponse("Upload file successfully",
                Map.of("file_path", filePath, "file_url", fileUrl)
        );
    }
}
