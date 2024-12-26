package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReferenceType implements CustomType{
    CONDITION("condition", "컨디션", "COND"),
    CONDITION_TYPE("conditionType", "컨디션 대분류", "COTY"),
    DEL_REASON("delReason", "탈퇴 사유", "DERE"),
    WEATHER("weather", "날씨", "WEAT");

    private final String type;
    private final String korean;
    private final String prefix;

    @Override
    public String toString() {
        return type;
    }

    @Override
    public String toKorean() {
        return korean;
    }

    public String toPrefix() {
        return prefix;
    }
}
