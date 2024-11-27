package com.soothee.oauth2.domain;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUser {
    private final OAuth2User oauth2User;

    /** 인증된 회원 정보를 받아서 Member 생성</hr>
     *
     * @return Member : 회원 엔티티
     */
    public Member toMember() {
        return Member.builder()
                    .email(this.getEmail())
                    .name(this.getNickName())
                    .snsType(SnsType.KAKAOTALK)
                    .oauth2ClientId(oauth2User.getName())
                    .build();
    }

    /** 인증 회원 프로퍼티 정보 가져오기 */
    private Map<String, Object> getProperties() {
        return oauth2User.getAttribute("properties");
    }

    /** 인증 회원 계정 정보 가져오기 */
    private Map<String, Object> getAccount() {
        return oauth2User.getAttribute("kakao_account");
    }

    /** 인증 회원 프로퍼티 정보 중 닉네임 가져오기 */
    private String getNickName() {
        return String.valueOf(this.getProperties().get("name"));
    }

    /** 인증 회원 계정 정보 중 아이디(이메일) 가져오기 */
    private String getEmail() {
        return String.valueOf(this.getAccount().get("email"));
    }
}
