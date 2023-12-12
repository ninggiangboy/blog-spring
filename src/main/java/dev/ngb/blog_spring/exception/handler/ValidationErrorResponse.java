package dev.ngb.blog_spring.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Value
public class ValidationErrorResponse {
    int status;
    String path;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp;
    String message;
    List<ValidationError> errors;

    @Value
    @Builder
    public static class ValidationError {
        String field;
        List<String> messages;
    }
}
