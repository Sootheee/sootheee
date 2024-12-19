package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "가장 높은/낮은 점수를 기록한 날짜와 해당 날짜의 고마운/배운 일")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class ConditionRatio {
    private Long condId;
    private Double condRatio;
}
