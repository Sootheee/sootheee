package com.soothee.oauth2.domain;

import com.soothee.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
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

    /** 인증된 회원 식별자 가져오기 */
    @Override
    public String getName() {
        return this.oauth2Id;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role).map(SimpleGrantedAuthority::new).toList();
    }

    /**
     * Member Entity 정보와 OAuth2User 정보로 AuthenticatedUser 생성</hr>
     *
     * @param member     Member : 서버에 저장된 회원 정보
     * @param oauth2User OAuth2User : 인증된 회원 정보
     * @return AuthenticatedUser 회원 정보
     */
    public static AuthenticatedUser of(Member member, OAuth2User oauth2User) {
        return AuthenticatedUser.builder()
                                .oauth2Id(member.getOauth2ClientId())
                                .role(member.getRole().getAuth())
                                .email(member.getEmail())
                                .memberName(member.getName())
                                .attributes(oauth2User.getAttributes())
                                .build();
    }
}

