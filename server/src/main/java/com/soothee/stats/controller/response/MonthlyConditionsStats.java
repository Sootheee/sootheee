package com.soothee.stats.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "한 달 동안 컨디션 요약")
public class MonthlyConditionsStats implements StatsResponse {
    @Schema(description = "한 달 동안 일기 컨디션 기록 횟수")
    private Integer count;

    @Schema(description = "한 달 동안 기록한 컨디션 중 선택된 비율이 높은 최대 3개의 컨디션 정보 리스트")
    private List<ConditionRatio> condiList;

    public MonthlyConditionsStats(Integer count) {
        this.count = count;
    }
}
