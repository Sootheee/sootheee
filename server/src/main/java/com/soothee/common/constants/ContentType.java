package com.soothee.common.constants;

import com.soothee.custom.exception.IncorrectParameterException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public enum ContentType implements CustomType {
    HOPE("hope", "바랐던 방향성"),
    THANKS("thanks", "감사한 일"),
    LEARN("learn", "배운 일");

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

    public static ContentType fromType(String type) throws IncorrectParameterException {
        if (StringUtils.equals(ContentType.HOPE.type, type)) {
            return ContentType.HOPE;
        }
        if (StringUtils.equals(ContentType.THANKS.type, type)) {
            return ContentType.THANKS;
        }
        if (StringUtils.equals(ContentType.LEARN.type, type)) {
            return ContentType.LEARN;
        }
        throw new IncorrectParameterException(ContentType.class, type);
    }
}
