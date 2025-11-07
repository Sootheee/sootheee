package com.soothee.dairy.controller.request;

import com.soothee.custom.valid.ExistReferenceId;
import com.soothee.custom.valid.YearRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 일기 수정
 * 1. 일기 일련번호 2. 작성날짜 3. 날씨 일련번호 3. 오늘의 점수 4. 선택한 컨디션 일련번호 리스트
 * 5. 오늘의 요약 6. 바랐던 방향성 7. 감사한 일 8. 배운 일
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "일기 수정 요청 파라미터")
public class DairyModifyRequest {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotEmpty(message = "일기의 날짜가 없습니다.")
    @YearRange(message = "날짜는 2024년부터, 2100년까지 입력 가능 합니다.")
    @Schema(description = "해당 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "일기의 날씨가 없습니다.")
    @ExistReferenceId(min = 1, max = 6, message = "존재하는 날씨 일련번호가 아닙니다.")
    @Schema(description = "날씨")
    private String weatherId;

    @NotEmpty(message = "오늘의 점수가 없습니다.")
    @PositiveOrZero(message = "오늘의 점수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @Schema(description = "선택한 컨디션들, 선택한 순서대로 전달됨")
    private List<@ExistReferenceId(min = 1, max = 15, message = "존재하는 컨디션 일련번호가 아닙니다.") String> condIdList;

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
    public DairyModifyRequest(Long dairyId, LocalDate date, String weatherId, Double score, String content, String hope, String thank, String learn) {
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