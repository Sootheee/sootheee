package com.soothee.common.requestParam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "조회할 년도/주차 파라미터")
public class WeekParam {
    @NotEmpty(message = "조회할 년도 정보가 없습니다.")
    @Schema(description = "조회할 년도")
    private Integer year;

    @NotEmpty(message = "조회할 주차 정보가 없습니다.")
    @Schema(description = "조회할 주차")
    private Integer week;
}
