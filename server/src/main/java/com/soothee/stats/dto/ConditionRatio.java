package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.*;
import lombok.Setter;

@NoArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "월간 통계 결과로 나온 컨디션의 정보")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class ConditionRatio {
    @NotEmpty(message = "컨디션의 일련번호가 없습니다.")
    @Schema(description = "컨디션의 일련번호")
    private Long condId;

    @NotEmpty(message = "컨디션의 비율 정보가 없습니다.")
    @Schema(description = "한 달간 전체 선택한 컨디션 중 해당 컨디션의 비율")
    private Double condRatio;
}
