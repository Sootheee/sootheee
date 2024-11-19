package com.soothee.common.domain;

import com.soothee.common.constants.Role;
import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUser {
    private final OAuth2User oauth2User;

    public Member toMember() {
        return Member.builder()
                    .email(this.getEmail())
                    .role(Role.USER)
                    .memberName(this.getNickName())
                    .snsType(SnsType.KAKAOTALK)
                    .oauth2ClientId(oauth2User.getName())
                    .build();
    }

    private Map<String, Object> getProperties() {
        return oauth2User.getAttribute("properties");
    }

    private Map<String, Object> getAccount() {
        return oauth2User.getAttribute("kakao_account");
    }

    private String getNickName() {
        return String.valueOf(this.getProperties().get("name"));
    }

    private String getEmail() {
        return String.valueOf(this.getAccount().get("email"));
    }
}
