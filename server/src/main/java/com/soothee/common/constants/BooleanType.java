package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BooleanType implements CustomType {
    DELETE("isDelete", "탈퇴 여부"),
    DARK_MODE("isDark", "다크모드");

    private final String type;
    private final String korean;

    @Override
    public String toString() {
        return type;
    }

    @Override
    public String toKorean() {
        return korean;
    }
}
