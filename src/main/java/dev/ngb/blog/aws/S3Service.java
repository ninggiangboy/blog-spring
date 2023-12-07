package dev.ngb.blog.aws;

public interface S3Service {
    void putFile(String fileName, String type, byte[] fileBytes);

    String getFileUrl(String filePath);

    boolean deleteFile(String fileName);
}
