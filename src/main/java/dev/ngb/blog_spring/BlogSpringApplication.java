package dev.ngb.blog_spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BlogSpringApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BlogSpringApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Server started");
    }
}
