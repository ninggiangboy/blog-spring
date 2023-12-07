package dev.ngb.blog.exception.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.ngb.blog.exception.*;
import dev.ngb.blog.exception.handler.ValidationErrorResponse.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request, HttpStatus status) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleStorageException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, List<String>> groupedErrors = new HashMap<>();
        Object targetObject = e.getBindingResult().getTarget();
        e.getBindingResult().getFieldErrors().forEach(
                error -> {
                    String fieldName = resolveFieldName(error.getField(), targetObject);
                    groupedErrors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(error.getDefaultMessage());
                }
        );
        List<ValidationError> errors = new ArrayList<>();
        groupedErrors.forEach((key, value) ->
                errors.add(ValidationError.builder()
                        .field(key)
                        .messages(value)
                        .build())
        );
        return ValidationErrorResponse.builder()
                .message("Invalid form data")
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String resolveFieldName(String fieldName, Object targetObject) {
        try {
            if (targetObject == null) {
                return fieldName;
            }
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
            if (jsonPropertyAnnotation != null) {
                return jsonPropertyAnnotation.value();
            }
        } catch (NoSuchFieldException e) {
            return fieldName;
        }
        return fieldName;
    }

    @ExceptionHandler({
            FileException.class,
            InvalidMediaTypeException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            NotEnoughException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundException.class,
            NoHandlerFoundException.class,
            NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            InsufficientAuthenticationException.class,
            AccessDeniedException.class,
            ExpiredException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            AuthenticationException.class,
            InvalidBearerTokenException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, request, HttpStatus.UNAUTHORIZED);
    }
}
