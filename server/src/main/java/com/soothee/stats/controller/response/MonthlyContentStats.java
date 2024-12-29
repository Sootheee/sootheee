package com.soothee.stats.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "한 달 동안 감사한/배운 일 요약")
public class MonthlyContentStats implements StatsResponse {
    @Schema(description = "한 달 동안 작성한 감사한/배운 일 횟수")
    private Integer count;

    @Schema(description = "한 달 중 가장 높은 점수를 준 날의 감사한/배운 일")
    private DateContents highest;

    @Schema(description = "한 달 중 가장 낮은 점수를 준 날의 감사한/배운 일")
    private DateContents lowest;

    public MonthlyContentStats(int count) {
        this.count = count;
    }
}
