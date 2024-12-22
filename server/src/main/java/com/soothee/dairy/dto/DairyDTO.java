package com.soothee.dairy.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "일기 수정/조회에 사용되는 DTO")
public class DairyDTO {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

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

    @Schema(description = "선택한 컨디션들, 선택한 순서대로 전달됨")
    private List<@Positive(message = "일련번호는 양수만 입력 가능합니다.")Long> condIds;

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

    @Builder
    @QueryProjection
    public DairyDTO(Long dairyId, LocalDate date, Long weatherId, Double score, String content, String hope, String thank, String learn) {
        this.dairyId = dairyId;
        this.date = date;
        this.weatherId = weatherId;
        this.score = score;
        this.content = content;
        this.hope = hope;
        this.thank = thank;
        this.learn = learn;
    }
}
