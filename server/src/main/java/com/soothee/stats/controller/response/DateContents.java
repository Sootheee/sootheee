package com.soothee.stats.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.custom.valid.YearRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 감사한/배운 일 조회
 * 1. 일기 일련번호 2. 작성 날짜 3. 오늘의 점수 4. 감사한/배운 일
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
@Schema(description = "해당 날짜의 감사한/배운 일 조회")
public class DateContents {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "감사한/배운 일 작성 날짜가 없습니다.")
    @YearRange(message = "날짜는 2024년부터, 2100년까지 입력 가능 합니다.")
    @Schema(description = "감사한/배운 일 작성 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @PositiveOrZero(message = "오늘의 점수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @NotBlank(message = "감사한/배운 일 내용이 없습니다.")
    @Size(max = 200, message = "바랐던 방향성은 최대 200자까지 입력 가능합니다.")
    @Schema(description = "감사한/배운 일 내용")
    private String content;
}
