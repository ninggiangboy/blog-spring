package dev.ngb.blog_spring.greeting;

import dev.ngb.blog_spring.base.BaseController;
import dev.ngb.blog_spring.base.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${prefix.api}")
@RequiredArgsConstructor
public class GreetingController extends BaseController {

    @GetMapping("/greeting")
    public ResponseEntity<ResultResponse> greeting() {
        return buildResponse("Hello World!");
    }
}
