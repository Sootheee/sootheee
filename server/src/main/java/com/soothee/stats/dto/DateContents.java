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
@Schema(description = "가장 높은/낮은 점수를 기록한 날짜와 해당 날짜의 고마운/배운 점")
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class DateContents {
    @NotEmpty(message = "일기의 일련번호가 없습니다.")
    @Schema(description = "일기 일련번호")
    private Long dairyId;

    @NotEmpty(message = "일기 작성 날짜가 없습니다.")
    @Schema(description = "가장 높은/낮은 점수를 기록한 날짜")
    private LocalDate date;

    @NotEmpty(message = "고마운/배운 점이 없습니다.")
    @Schema(description = "그 날의 고마운/배운 점")
    private String content;
}
