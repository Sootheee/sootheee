package com.soothee.stats.controller;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.stats.dto.WeeklyStatsDTO;
import com.soothee.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Tag(name = "Stats API", description = "통계 관련 처리")
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;

    /** 통계 월간 요약 */
    @GetMapping("/monthly")
    @Operation(summary = "통계 월간 평균 요약", description = "로그인한 계정의 해당 월 일기 작성 횟수, 평균 점수, 최빈 컨디션에 대한 정보", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 일기 수 부족", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyStats(@RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(loginInfo, year, month);
        if (result.getDairyCnt() < 5) {
            return new ResponseEntity<>(MyErrorMsg.NOT_ENOUGH_DAIRY_COUNT, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<MonthlyStatsDTO>(result, HttpStatus.OK);
    }

    /** 통계 주간 요약 */
    @GetMapping("/weekly")
    @Operation(summary = "통계 주간 평균 요약", description = "로그인한 계정의 해당 주 일기 작성 횟수, 평균 점수, 일간 점수", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/weekly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "week", description = "조회할 주", example = "/stats/weekly?year=2024&week=30", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = WeeklyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 일기 수 부족", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendWeeklyStats(@RequestParam("year") Integer year,
                                             @RequestParam("week") Integer week,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(loginInfo, year, week);
        if (result.getDairyCnt() < 5) {
            return new ResponseEntity<>(MyErrorMsg.NOT_ENOUGH_DAIRY_COUNT, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<WeeklyStatsDTO>(HttpStatus.OK);
    }
}
