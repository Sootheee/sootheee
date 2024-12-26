package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.DoubleType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.ExistReferenceId;
import com.soothee.custom.valid.SootheeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "한 달 동안 작성된 일기 정보 조회")
public class MonthlyStatsDTO {
    @NotEmpty(message = "한 달 동안 작성한 일기의 갯수가 없습니다.")
    @PositiveOrZero(message = "작성 일기 갯수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 작성 일기 갯수")
    private Integer count;

    @NotEmpty(message = "한 달 동안 오늘의 점수 평균값이 없습니다.")
    @PositiveOrZero(message = "오늘의 점수의 평균값은 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 오늘의 점수 평균값")
    private Double scoreAvg;

    @NotBlank(message = "한 달 동안 가장 많이 선택한 컨디션 정보가 없습니다.")
    @ExistReferenceId(min = 1,max = 15,message = "존재하는 컨디션 일련번호가 아닙니다.")
    @Schema(description = "한 달 동안 가장 많이 선택한 1개의 컨디션의 일련번호, 선택된 컨디션이 1개도 없는 경우 0 || \n" +
            " [컨디션 선택 횟수가 같은 경우] (1) 먼저 선택한 순 (2) 긍정 > 보통 > 부정 카테고리 순 (3) 카테고리별 먼저 등록된 순")
    private String mostCondId;

    @NotEmpty(message = "한 달 동안 가장 많이 선택한 컨디션 비율 정보가 없습니다.")
    @PositiveOrZero(message = "비율은 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 달 동안 가장 많이 선택한 1개의 컨디션 비율")
    private Double mostCondRatio;

    @QueryProjection
    public MonthlyStatsDTO(Integer count, Double scoreAvg) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkInteger(count, DomainType.DAIRY);
        SootheeValidation.checkDouble(scoreAvg, DoubleType.AVG);
        this.count = count;
        this.scoreAvg = scoreAvg;
    }
}
