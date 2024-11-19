package com.soothee.common.domain;

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
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser implements Principal, OAuth2User {
    /** 회원 일련번호 */
    private String oauth2Id;
    /** 회원 이메일 */
    private String email;
    private List<String> roles;
    /** 회원 닉네임 */
    private String memberName;
    private Map<String, Object> attributes;

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
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
    }

    public static AuthenticatedUser of(Member member, OAuth2User oauth2User) {
        return AuthenticatedUser.builder()
                                .oauth2Id(String.valueOf(member.getOauth2ClientId()))
                                .roles(List.of(member.getRole().getRole()))
                                .email(member.getEmail())
                                .memberName(member.getMemberName())
                                .attributes(oauth2User.getAttributes())
                                .build();
    }
}

