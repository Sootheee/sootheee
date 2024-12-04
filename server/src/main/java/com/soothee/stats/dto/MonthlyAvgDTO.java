package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "월간 일기 작성 요약")
public class MonthlyAvgDTO {

    @NotEmpty(message = "월간 작성한 일기의 개수가 없습니다.")
    @Schema(description = "월간 작성 일기 개수")
    private Integer dairyCnt;

    @NotEmpty(message = "월간 오늘의 점수 평균값이 없습니다.")
    @Schema(description = "월간 오늘의 점수 평균값")
    private Double scoreAvg;

    @Schema(description = "월간 가장 많이 선택한 1개의 컨디션의 일련번호, 선택된 컨디션이 1개도 없는 경우 0")
    private Long mostCondId;

    @QueryProjection
    public MonthlyAvgDTO(Integer dairyCnt, Double scoreAvg) {
        this.dairyCnt = dairyCnt;
        this.scoreAvg = scoreAvg;
    }
}
