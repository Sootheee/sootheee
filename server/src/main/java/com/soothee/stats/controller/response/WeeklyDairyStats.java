package com.soothee.stats.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
@Schema(description = "한 주 동안 작성한 일기 통계 요약")
public class WeeklyDairyStats implements StatsResponse {
    @Schema(description = "한 주 동안 작성 일기 갯수")
    private Integer count;

    @Schema(description = "한 주 동안 오늘의 점수 평균값")
    private Double scoreAvg;

    @Schema(description = "한 주 동안 작성된 해당 날짜의 오늘의 점수 Map")
    private List<DateScore> scoreList;

    @QueryProjection
    public WeeklyDairyStats(Integer count, Double scoreAvg) {
        this.count = count;
        this.scoreAvg = scoreAvg;
    }
}
