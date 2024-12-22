package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.DoubleType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
    @NotEmpty(message = "컨디션의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "컨디션의 일련번호")
    private Long condId;

    @NotEmpty(message = "컨디션의 비율 정보가 없습니다.")
    @PositiveOrZero(message = "비율은 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 선택한 모든 컨디션 중 해당 컨디션의 비율")
    private Double condRatio;

    @QueryProjection
    public ConditionRatio(Long condId, Double condRatio) throws IncorrectValueException, NullValueException {
        checkConstructorConditionRatio(condId, condRatio);
        this.condId = condId;
        this.condRatio = condRatio;
    }

    /** validation */
    private static void checkConstructorConditionRatio(Long condId, Double condRatio) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(condId, DomainType.CONDITION);
        SootheeValidation.checkDouble(condRatio, DoubleType.RATIO);
    }
}
