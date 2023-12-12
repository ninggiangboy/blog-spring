package dev.ngb.blog_spring.exception;

public class NotEnoughException extends RuntimeException {
    public NotEnoughException(String message) {
        super(message);
    }
}
