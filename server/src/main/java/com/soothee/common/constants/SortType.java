package com.soothee.common.constants;

import com.soothee.custom.exception.IncorrectParameterException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public enum SortType implements CustomType {
    DATE("date", "오래된 날짜순"),
    HIGH("high", "높은 점수순"),
    LOW("low", "낮은 점수순");

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

    public static SortType fromType(String type) throws IncorrectParameterException {
        if (StringUtils.equals(SortType.DATE.type, type)) {
            return SortType.DATE;
        }
        if (StringUtils.equals(SortType.HIGH.type, type)) {
            return SortType.HIGH;
        }
        if (StringUtils.equals(SortType.LOW.type, type)) {
            return SortType.LOW;
        }
        throw new IncorrectParameterException(SortType.class, type);
    }
}
