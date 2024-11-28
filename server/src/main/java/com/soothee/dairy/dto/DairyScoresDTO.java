package com.soothee.dairy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "해당 월에 작성한 모든 일기의 오늘의 점수 정보")
public class DairyScoresDTO {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "일기의 날짜가 없습니다.")
    @Schema(description = "일기 날짜")
    private LocalDate date;

    @NotEmpty(message = "일기의 오늘의 점수가 없습니다.")
    @Schema(description = "오늘의 점수")
    private Double score;
}
