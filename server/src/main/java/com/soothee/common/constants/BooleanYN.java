package com.soothee.common.constants;

import com.soothee.custom.exception.IncorrectParameterException;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public enum BooleanYN implements CustomType {
    Y("Y", "예"), N("N", "아니오");

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

    public static BooleanYN fromString(String yn) throws IncorrectParameterException, NullValueException, IncorrectValueException {
        SootheeValidation.checkBoolean(yn, BooleanType.DARK_MODE);
        if (StringUtils.equals(BooleanYN.Y.toString(), yn)) {
            return BooleanYN.Y;
        }
        if (StringUtils.equals(BooleanYN.N.toString(), yn)) {
            return BooleanYN.N;
        }
        throw new IncorrectParameterException(BooleanYN.class, yn);
    }
}
