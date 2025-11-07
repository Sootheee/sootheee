package com.soothee.dairy.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 해당 월에 작성한 모든 일기
 * 1. 일기 일련번호 2. 작성 날짜 3. 오늘의 점수
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
@Schema(description = "해당 월에 작성한 모든 일기의 오늘의 점수 정보")
public class DairyScoresResponse {
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "일기 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "오늘의 점수")
    private Double score;
}
