package dev.ngb.blog_spring.token;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private UUID userId;
    private String ipAddress;
    private String userAgent;
}
