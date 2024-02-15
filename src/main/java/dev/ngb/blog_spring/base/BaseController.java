package dev.ngb.blog_spring.base;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@RequiredArgsConstructor
public class BaseController {
    protected ResponseEntity<ResultResponse> buildResponse(String message, Object result, HttpStatus status) {
        result = result instanceof Page<?> page ? getPageInfo(page) : Map.of("data", result);
        return ResponseEntity.status(status).body(
                ResultResponse.builder()
                        .result(result)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    private Map<String, Object> getPageInfo(Page<?> page) {
        return Map.of(
                "data", page.getContent(),
                "page_meta", MetaPage.builder()
                        .perPage(page.getSize())
                        .page(page.getNumber() + 1)
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .hasNext(page.hasNext())
                        .hasPrev(page.hasPrevious())
                        .build()
        );
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message, Object result) {
        return buildResponse(message, result, HttpStatus.OK);
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message, HttpStatus status) {
        return buildResponse(message, new ArrayList<>(), status);
    }

    protected ResponseEntity<ResultResponse> buildResponse(String message) {
        return buildResponse(message, new ArrayList<>(), HttpStatus.OK);
    }

    protected ResponseEntity<ResultResponse> buildResponse() {
        return buildResponse("", new ArrayList<>(), HttpStatus.OK);
    }
}
