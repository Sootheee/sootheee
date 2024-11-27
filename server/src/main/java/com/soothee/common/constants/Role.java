package com.soothee.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자", "ROLE_ADMIN"), USER("회원", "ROLE_USER");

    private final String role;
    @Getter
    private final String auth;

    public String toString(){
        return role;
    }
}
