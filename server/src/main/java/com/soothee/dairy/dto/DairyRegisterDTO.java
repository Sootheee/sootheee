package com.soothee.dairy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "일기 등록에 사용되는 DTO")
public class DairyRegisterDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotEmpty(message = "일기의 날짜가 없습니다.")
    @Schema(description = "해당 날짜")
    private LocalDate date;

    @NotEmpty(message = "일기의 날씨가 없습니다.")
    @Schema(description = "날씨")
    private Long weatherId;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @Schema(description = "선택한 컨디션들")
    private List<Long> condIdList;

    @Schema(description = "오늘의 요약")
    private String content;

    @Schema(description = "바랐던 방향성")
    private String hope;

    @Schema(description = "감사한 일")
    private String thank;

    @Schema(description = "배운 일")
    private String learn;
}
