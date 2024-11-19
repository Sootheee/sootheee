package com.soothee.common.domain;

import com.soothee.common.constants.Role;
import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleUser {
    private final OAuth2User oauth2User;

    public Member toMember() {
        return Member.builder()
                    .email(this.getEmail())
                    .role(Role.USER)
                    .memberName(this.getNickName())
                    .snsType(SnsType.GOOGLE)
                    .oauth2ClientId(oauth2User.getName())
                    .build();
    }

    private Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    private String getNickName() {
        return String.valueOf(this.getAttributes().get("name"));
    }

    private String getEmail() {
        return String.valueOf(this.getAttributes().get("email"));
    }
}
