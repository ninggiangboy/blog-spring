package dev.ngb.blog.aws;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.credentials")
@Value
public class AWSCredentialsProperties {
    String accessKey;
    String secretKey;
}
