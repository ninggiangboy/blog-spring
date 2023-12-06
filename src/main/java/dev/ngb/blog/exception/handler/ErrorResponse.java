package dev.ngb.blog.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class ErrorResponse {
    int status;
    String path;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp;
    String message;
}

