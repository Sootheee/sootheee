package com.soothee.dairy.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 일기 조회
 * 1. 일기 일련번호 2. 작성날짜 3. 날씨 일련번호 3. 오늘의 점수 4. 선택한 컨디션 일련번호 리스트
 * 5. 오늘의 요약 6. 바랐던 방향성 7. 감사한 일 8. 배운 일
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "일기 조회")
public class DairyAllResponse {
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "해당 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "날씨")
    private String weatherId;

    @Schema(description = "오늘의 점수")
    private Double score;

    @Schema(description = "선택한 컨디션들, 선택한 순서대로 전달됨")
    private List<String> condIdList;

    @Schema(description = "오늘의 요약")
    private String content;

    @Schema(description = "바랐던 방향성")
    private String hope;

    @Schema(description = "감사한 일")
    private String thank;

    @Schema(description = "배운 일")
    private String learn;

    @Builder
    @QueryProjection
    public DairyAllResponse(Long dairyId, LocalDate date, String weatherId, Double score, String content, String hope, String thank, String learn) {
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
