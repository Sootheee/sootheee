package com.soothee.stats.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * 조회할 날짜에 작성한 일기와 오늘의 점수
 * 1. 일기 일련번호 2. 작성 날짜 3. 오늘의 점수
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
@Schema(description = "일기 작성 날짜와 해당 일기의 오늘의 점수")
public class DateScore {
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @Schema(description = "일기 작성 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "오늘의 점수")
    private Double score;
}
