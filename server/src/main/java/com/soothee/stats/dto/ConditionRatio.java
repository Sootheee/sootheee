package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.DoubleType;
import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.ExistReferenceId;
import com.soothee.custom.valid.SootheeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

/**
 * 한 달 동안 컨디션 통계
 * 1. 컨디션 일련번호 2. 한 달 동안 선택한 모든 컨디션 중 해당 컨디션의 비율
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "한 달 동안 컨디션 통계 정보")
public class ConditionRatio {
    @NotBlank(message = "컨디션의 일련번호가 없습니다.")
    @ExistReferenceId(min = 1, max = 15, message = "존재하는 컨디션 일련번호가 아닙니다.")
    @Schema(description = "컨디션의 일련번호")
    private String condId;

    @NotEmpty(message = "컨디션의 비율 정보가 없습니다.")
    @PositiveOrZero(message = "비율은 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 선택한 모든 컨디션 중 해당 컨디션의 비율")
    private Double condRatio;

    @QueryProjection
    public ConditionRatio(String condId, Double condRatio) {
        this.condId = condId;
        this.condRatio = condRatio;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkReferenceId(getCondId(), ReferenceType.CONDITION);
        SootheeValidation.checkDouble(getCondRatio(), DoubleType.RATIO);
    }
}
