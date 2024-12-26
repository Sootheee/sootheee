package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.DoubleType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "한 주 동안 작성한 일기 통계 요약")
public class WeeklyStatsDTO {
    @NotEmpty(message = "한 주 동안 작성한 일기의 갯수가 없습니다.")
    @PositiveOrZero(message = "작성 일기 갯수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 주 동안 작성 일기 갯수")
    private Integer count;

    @NotEmpty(message = "한 주 동안 오늘의 점수 평균값이 없습니다.")
    @PositiveOrZero(message = "오늘의 점수 평균값은 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "한 주 동안 오늘의 점수 평균값")
    private Double scoreAvg;

    @NotEmpty(message = "일주일 동안 작성된 오늘의 점수이 없습니다.")
    @Schema(description = "한 주 동안 작성된 해당 날짜의 오늘의 점수 Map")
    private List<DateScore> scoreList;

    @QueryProjection
    public WeeklyStatsDTO(Integer count, Double scoreAvg) {
        this.count = count;
        this.scoreAvg = scoreAvg;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkInteger(count, DomainType.DAIRY);
        SootheeValidation.checkDouble(scoreAvg, DoubleType.AVG);
    }
}
