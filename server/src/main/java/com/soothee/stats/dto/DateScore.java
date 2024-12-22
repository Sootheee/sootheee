package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

/**
 * 지정한 날짜에 작성한 일기와 오늘의 점수
 * 1. 일기 일련번호 2. 작성 날짜 3. 오늘의 점수
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "일기 작성 날짜와 해당 일기의 오늘의 점수")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class DateScore {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "일기 작성 날짜가 없습니다.")
    @YearRange(message = "날짜는 2024년부터, 2100년까지 입력 가능 합니다.")
    @Schema(description = "일기 작성 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @PositiveOrZero(message = "오늘의 점수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "오늘의 점수")
    private Double score;
}
