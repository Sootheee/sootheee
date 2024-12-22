package com.soothee.dairy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 일기 등록
 * 1. 작성 날짜 2. 날씨 일련번호 3. 오늘의 점수 4. 선택한 컨디션 일련번호 리스트
 * 5. 오늘의 요약 6. 바랐던 방향성 7. 감사한 일 8. 배운 일
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "일기 등록에 사용되는 DTO")
public class DairyRegisterDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotEmpty(message = "일기의 날짜가 없습니다.")
    @YearRange(message = "날짜는 2024년부터, 2100년까지 입력 가능 합니다.")
    @Schema(description = "해당 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty(message = "일기의 날씨가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "날씨")
    private Long weatherId;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @PositiveOrZero(message = "오늘의 점수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @Schema(description = "선택한 컨디션들")
    private List<@Positive(message = "일련번호는 양수만 입력 가능합니다.")Long> condIdList;

    @Size(max = 600, message = "오늘의 요약은 최대 600자까지 입력 가능합니다.")
    @Schema(description = "오늘의 요약")
    private String content;

    @Size(max = 200, message = "바랐던 방향성은 최대 200자까지 입력 가능합니다.")
    @Schema(description = "바랐던 방향성")
    private String hope;

    @Size(max = 200, message = "감사한 일은 최대 200자까지 입력 가능합니다.")
    @Schema(description = "감사한 일")
    private String thank;

    @Size(max = 200, message = "배운 일은 최대 600자까지 입력 가능합니다.")
    @Schema(description = "배운 일")
    private String learn;
}
