package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "주간 일기 작성 요약")
public class WeeklyStatsDTO {
    @NotEmpty(message = "주간 작성한 일기의 개수가 없습니다.")
    @Schema(description = "주간 작성 일기 개수")
    private Integer count;

    @NotEmpty(message = "주간 오늘의 점수 평균값이 없습니다.")
    @Schema(description = "주간 오늘의 점수 평균값")
    private Double scoreAvg;

    @NotEmpty(message = "일주일 동안 작성된 오늘의 점수이 없습니다.")
    @Schema(description = "주간 작성된 해당 날짜의 오늘의 점수 Map")
    private List<DateScore> scoreList;

    @QueryProjection
    public WeeklyStatsDTO(Integer count, Double scoreAvg) {
        this.count = count;
        this.scoreAvg = scoreAvg;
    }
}
