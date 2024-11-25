package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"), USER("회원");

    private final String role;

    public String toString(){
        return role;
    }
}
