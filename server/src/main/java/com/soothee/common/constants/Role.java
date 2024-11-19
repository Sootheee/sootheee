package com.soothee.common.constants;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("관리자"), USER("회원");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
