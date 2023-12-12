package dev.ngb.blog_spring.exception;

public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }
}
