package com.soothee.oauth2.userDomain;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class GoogleUser {
    private final OAuth2User oauth2User;

    /**
     * 인증된 회원 정보를 받아서 Member 생성
     *
     * @return Member entity
     */
    public Member toMember() {
        return Member.builder()
                    .email(getEmail())
                    .name(getNickName())
                    .snsType(SnsType.GOOGLE)
                    .oauth2ClientId(oauth2User.getName())
                    .build();
    }

    /** 인증 회원 계정 정보 가져오기 */
    private Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    /** 인증 회원 정보 중 닉네임 가져오기 */
    private String getNickName() {
        String nickname = String.valueOf(getAttributes().get("name"));
        if (nickname.isBlank()) {
            throw new IllegalStateException("Nickname is missing in Google OAuth2 response");
        }
        return nickname;
    }

    /** 인증 회원 정보 중 아이디(이메일) 가져오기 */
    private String getEmail() {
        String email = String.valueOf(getAttributes().get("email"));
        if (email.isBlank()) {
            throw new IllegalStateException("Email is missing in Google OAuth2 response");
        }
        return email;
    }
}
