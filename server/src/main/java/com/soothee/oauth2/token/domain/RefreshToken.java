package com.soothee.oauth2.token.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    /** 사용자 ID */
    private String oauth2ClientId;
    /** Refresh Token */
    private String refreshToken;
    /** 만료 시간 (ms) */
    private Long expiration;
}
