package dev.ngb.blog_spring.aws;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AWSCredentialsProperties.class)
public class AWSConfig {
}
