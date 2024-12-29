package com.soothee.stats.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 한 달 동안 컨디션 통계
 * 1. 컨디션 일련번호 2. 한 달 동안 선택한 모든 컨디션 중 해당 컨디션의 비율
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
@Schema(description = "한 달 동안 컨디션 통계 정보")
public class ConditionRatio {
    @Schema(description = "컨디션의 일련번호")
    private String condId;

    @Schema(description = "한 달 동안 선택한 모든 컨디션 중 해당 컨디션의 비율")
    private Double condRatio;
}
