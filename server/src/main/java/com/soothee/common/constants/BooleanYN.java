package com.soothee.common.constants;

import com.soothee.custom.exception.IncorrectParameterException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public enum BooleanYN implements CustomType {
    Y("Y"), N("N");

    private final String type;

    @Override
    public String toString() {
        return type;
    }

    @Override
    public String toKorean() {
        return type;
    }

    public static BooleanYN fromString(String yn) throws IncorrectParameterException {
        if (StringUtils.equals(BooleanYN.Y.toString(), yn)) {
            return BooleanYN.Y;
        }
        if (StringUtils.equals(BooleanYN.N.toString(), yn)) {
            return BooleanYN.N;
        }
        throw new IncorrectParameterException(BooleanYN.class, yn);
    }
}
