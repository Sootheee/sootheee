package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "해당 날짜의 감사한/배운 일 조회")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class DateContents {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "감사한/배운 일 작성 날짜가 없습니다.")
    @Schema(description = "감사한/배운 일 작성 날짜")
    private LocalDate date;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @NotEmpty(message = "감사한/배운 일 내용이 없습니다.")
    @Schema(description = "감사한/배운 일 내용")
    private String content;
}
