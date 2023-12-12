package dev.ngb.blog_spring.exception;

public class InvalidLocation extends RuntimeException {
    public InvalidLocation(String message) {
        super(message);
    }
}
