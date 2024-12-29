package com.soothee.custom.valid;

import com.soothee.common.constants.ReferenceType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ExistReferenceIdValidator implements ConstraintValidator<ExistReferenceId, String> {
    private int idLength;
    private int min;
    private int max;

    @Override
    public void initialize(ExistReferenceId constraintAnnotation) {
        this.idLength = constraintAnnotation.idLength();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    /**
     * 컨디션/컨디션 타입/탈퇴 사유/날씨의 일련번호 검증
     * 입력된 일련번호가
     * 1. null, " ", "" 이 아니고
     * 2. 7자리 이고
     * 3. 컨디션/컨디션 타입/탈퇴 사유/날씨의 일련번호의 형식으로 올바른지
     * 4. 실제로 존재하는 일련번호인지
     * 검증
     *
     * @param string 입력된 일련번호
     * @param constraintValidatorContext ConstraintValidatorContext
     * @return 맞으면 true / 아니면 false
     */
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        String prefix = StringUtils.substring(string, 0, 4);
        int id = Integer.parseInt(StringUtils.substring(string, 4));

        return StringUtils.isNotBlank(string)
                && string.length() == idLength
                && (StringUtils.equals(prefix, ReferenceType.CONDITION.toPrefix())
                    || StringUtils.equals(prefix, ReferenceType.CONDITION_TYPE.toPrefix())
                    || StringUtils.equals(prefix, ReferenceType.DEL_REASON.toPrefix())
                    || StringUtils.equals(prefix, ReferenceType.WEATHER.toPrefix()))
                && id > (min - 1) && id < (max + 1);
    }
}
