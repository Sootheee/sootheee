package com.soothee.stats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "월간 작성한 모든 감사한/배운 일 리스트 조회")
@AllArgsConstructor
@Builder
public class MonthlyAllContentsDTO {
    @NotEmpty(message = "월간 작성 횟수가 없습니다.")
    @Schema(description = "한 달 동안 작성한 감사한/배운 일 횟수")
    private Integer count;

    @NotEmpty(message = "작성한 감사한/배운 일이 없습니다.")
    @Schema(description = "한 달 중 가장 높은 점수를 준 날의 감사한/배운 일")
    private List<DateContents> contentList;
}
