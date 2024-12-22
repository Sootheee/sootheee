package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "월간 작성된 일기 정보 조회")
public class MonthlyStatsDTO {

    @NotEmpty(message = "월간 작성한 일기의 개수가 없습니다.")
    @Schema(description = "월간 작성 일기 개수")
    private Integer count;

    @NotEmpty(message = "월간 오늘의 점수 평균값이 없습니다.")
    @Schema(description = "월간 오늘의 점수 평균값")
    private Double scoreAvg;

    @Schema(description = "월간 가장 많이 선택한 1개의 컨디션의 일련번호, 선택된 컨디션이 1개도 없는 경우 0 || \n" +
            " [컨디션 선택 횟수가 같은 경우] (1) 먼저 선택한 순 (2) 긍정 > 보통 > 부정 카테고리 순 (3) 카테고리별 먼저 등록된 순")
    private Long mostCondId;

    @Schema(description = "월간 가장 많이 선택한 1개의 컨디션 비율")
    private Double mostCondRatio;

    @QueryProjection
    public MonthlyStatsDTO(Integer count, Double scoreAvg) {
        this.count = count;
        this.scoreAvg = scoreAvg;
    }
}
