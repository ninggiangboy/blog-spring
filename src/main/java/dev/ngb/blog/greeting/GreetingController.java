package dev.ngb.blog.greeting;

import dev.ngb.blog.base.BaseController;
import dev.ngb.blog.base.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greeting")
@RequiredArgsConstructor
public class GreetingController extends BaseController {

    @GetMapping
    public ResponseEntity<ResultResponse> greeting() {
        return buildResponse("Hello World!");
    }
}
