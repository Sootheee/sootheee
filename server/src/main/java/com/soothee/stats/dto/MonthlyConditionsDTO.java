package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "월간 컨디션 요약")
public class MonthlyConditionsDTO {
    @NotEmpty(message = "월간 컨디션 기록 횟수가 없습니다.")
    @Schema(description = "월간 일기 컨디션 기록 횟수")
    private Integer count;

    @NotEmpty(message = "월간 기록된 컨디션 중 비율이 높은 최대 3개의 컨디션 정보 리스트가 없습니다.")
    @Schema(description = "월간 기록한 컨디션 중 선택된 비율이 높은 최대 3개의 컨디션 정보 리스트")
    private List<ConditionRatio> condiList;

    @QueryProjection
    public MonthlyConditionsDTO(Integer count) {
        this.count = count;
    }
}
