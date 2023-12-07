package dev.ngb.blog.aws;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.s3")
@Value
public class S3Properties {
    String awsRegion;
    String bucketName;
}
