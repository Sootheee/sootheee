package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainType implements CustomType {
    DAIRY("dairy", "일기"),
    DAIRY_CONDITION("dairyCondition", "일기 컨디션"),
    MEMBER("member", "회원"),
    MEMBER_DEL_REASON("memberDelReason", "회원 탈퇴 사유"),
    CONDITION("condition", "컨디션"),
    CONDITION_TYPE("conditionType", "컨디션 대분류"),
    DEL_REASON("delReason", "탈퇴 사유"),
    WEATHER("weather", "날씨");

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
