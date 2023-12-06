package dev.ngb.blog;

import dev.ngb.blog.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BlogSpringApplication implements CommandLineRunner {

    private final EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(BlogSpringApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Spring Boot");
    }
}
