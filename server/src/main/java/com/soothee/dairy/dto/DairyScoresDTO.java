package com.soothee.dairy.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.DoubleType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.custom.valid.YearRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 해당 월에 작성한 모든 일기
 * 1. 일기 일련번호 2. 작성 날짜 3. 오늘의 점수
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Schema(description = "해당 월에 작성한 모든 일기의 오늘의 점수 정보")
public class DairyScoresDTO {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Positive(message = "일련번호는 양수만 입력 가능합니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotEmpty(message = "일기의 날짜가 없습니다.")
    @YearRange(message = "날짜는 2024년부터, 2100년까지 입력 가능 합니다.")
    @Schema(description = "일기 날짜, format = yyyy-MM-dd")
    private LocalDate date;

    @NotEmpty(message = "일기의 오늘의 점수가 없습니다.")
    @PositiveOrZero(message = "오늘의 점수는 0을 포함한 양수만 입력 가능합니다.")
    @Schema(description = "오늘의 점수")
    private Double score;

    @QueryProjection
    public DairyScoresDTO(Long dairyId, LocalDate date, Double score) throws IncorrectValueException, NullValueException {
        checkConstructorDairyScoresDTO(dairyId, date, score);
        this.dairyId = dairyId;
        this.date = date;
        this.score = score;
    }

    /** validation */
    private static void checkConstructorDairyScoresDTO(Long dairyId, LocalDate date, Double score) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(dairyId, DomainType.DAIRY);
        SootheeValidation.checkDate(date);
        SootheeValidation.checkDouble(score, DoubleType.SCORE);
    }
}
