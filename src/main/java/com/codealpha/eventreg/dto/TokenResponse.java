package com.codealpha.eventreg.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;
    private String role;
}
