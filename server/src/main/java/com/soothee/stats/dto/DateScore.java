package com.soothee.stats.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "일기 작성 날짜와 해당 일기의 오늘의 점수")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class DateScore {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "일기 작성 날짜가 없습니다.")
    @Schema(description = "일기 작성 날짜")
    private LocalDate date;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @Schema(description = "오늘의 점수")
    private Double score;
}
