package dev.ngb.blog_spring.token;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig {
}
