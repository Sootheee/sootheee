package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StringType implements CustomType {
    EMAIL("email", "회원 이메일"),
    NAME("name", "회원 닉네임"),
    DATE("date", "작성 날짜"),
    CONTENT("content", "오늘의 요약"),
    SNS_TYPE("SnsType", "SNS 종류"),
    OAUTH2("oauth2ClientId", "인증 일련번호"),
    ORDER_NO("orderNO", "선택 순서");

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
