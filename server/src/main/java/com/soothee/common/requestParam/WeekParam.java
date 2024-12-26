package com.soothee.common.requestParam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "조회할 년도/주차 파라미터")
public class WeekParam {
    @NotEmpty(message = "조회할 년도 정보가 없습니다.")
    @Min(value = 2024, message = "조회할 년도는 2024부터 2100까지만 입력가능 합니다.")
    @Max(value = 2100, message = "조회할 년도는 2024부터 2100까지만 입력가능 합니다.")
    @Schema(description = "조회할 년도")
    private Integer year;

    @NotEmpty(message = "조회할 주차 정보가 없습니다.")
    @Min(value = 1, message = "조회할 주차는 1부터 53까지만 입력가능 합니다.")
    @Max(value = 53, message = "조회할 주차는 1부터 53까지만 입력가능 합니다.")
    @Schema(description = "조회할 주차")
    private Integer week;
}
