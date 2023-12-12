package dev.ngb.blog_spring.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
}
