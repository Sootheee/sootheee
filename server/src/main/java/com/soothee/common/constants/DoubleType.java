package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DoubleType implements CustomType {
    SCORE("score", "점수"),
    AVG("avg", "평균 점수"),
    RATIO("ratio", "점수 비율");

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
