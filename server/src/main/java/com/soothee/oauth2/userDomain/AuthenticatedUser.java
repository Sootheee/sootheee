package com.soothee.oauth2.userDomain;

import com.soothee.member.domain.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 인증된 사용자 정보를 시큐리티 컨텍스트(security context)에 보관할 때 사용
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class AuthenticatedUser implements Principal, OAuth2User {
    /** OAuth2 인증 회원 식별자 */
    private String oauth2Id;
    /** 회원 이메일 */
    private String email;
    /** 회원 권한 */
    private String role;
    /** 회원 닉네임 */
    private String memberName;
    /** 회원 정보 */
    private Map<String, Object> attributes;
    private Long memberId;

    /** 인증된 회원 식별자 가져오기 */
    @Override
    public String getName() {
        return oauth2Id;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role).map(SimpleGrantedAuthority::new).toList();
    }

    /**
     * Member Entity 정보와 OAuth2User 정보로 AuthenticatedUser 생성
     *
     * @param member 서버에 등록된 회원 정보
     * @param oauth2User 인증된 회원 정보
     * @return AuthenticatedUser 회원 정보
     */
    public static AuthenticatedUser of(Member member, OAuth2User oauth2User) {
        return AuthenticatedUser.builder()
                                .oauth2Id(member.getOauth2ClientId())
                                .role(member.getRole().getAuth())
                                .email(member.getEmail())
                                .memberId(member.getMemberId())
                                .memberName(member.getName())
                                .attributes(oauth2User.getAttributes())
                                .build();
    }
}

