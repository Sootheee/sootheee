package com.soothee.stats.controller;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.custom.exception.*;
import com.soothee.oauth2.userDomain.AuthenticatedUser;
import com.soothee.stats.controller.response.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.soothee.custom.valid.SootheeValidation.validYearMonthPathParam;
import static com.soothee.custom.valid.SootheeValidation.validYearWeekPathParam;

@Controller
@RequiredArgsConstructor
@Tag(name = "Stats API", description = "월간/주간 동안 통계 조회")
@Slf4j
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;

    /** 월간 일기 요약 통계 조회 */
    @GetMapping("/monthly/{year}/{month}")
    @Operation(summary = "월간 일기 요약 통계 조회", description = "로그인한 계정의 해당 년도/월 일기 작성 횟수, 평균 점수, 가장 많이 선택한 컨디션에 대한 정보 조회 - 통계 가능 최소 일기 작성 개수 3개", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/2024/2", required = true, in = ParameterIn.PATH),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/2024/10", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 일기 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyDairyStats.class))),
            @ApiResponse(responseCode = "204", description = "해당 월에 작성한 일기가 통계 가능 최소 일기 작성 수(3)보다 적어 통계 불가", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 2개 이상 조회 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 통계 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyStats(@PathVariable("year") int year, @PathVariable("month") int month,
                                                @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearMonthPathParam(year, month);
            Long loginId = loginInfo.getMemberId();
            MonthlyDairyStats result = statsService.getMonthlyDairyStats(loginId, year, month);
            if (ifNotEnoughDairyForStats(result)) {
                return new ResponseEntity<>("통계 가능 최소 일기 작성 개수(3)를 만족하지 못해 통계 불가", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ErrorToSearchStatsException e) {
            log.error("통계 결과 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            log.error("통계 결과가 2개 이상 조회 - 409\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            log.error("선택한 컨디션 통계 정보 조회 실패 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 감사한/배운 일 요약 통계 조회 */
    @GetMapping("/monthly/{type}/{year}/{month}")
    @Operation(summary = "월간 감사한/배운 일 요약 통계 조회", description = "로그인한 계정의 해당 년도/월 감사한/배운 일 작성 횟수, 가장 높은/낮은 점수의 감사한/배운 일 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "감사한(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks/2024/2", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/thanks/2024/10", required = true, in = ParameterIn.PATH),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/learn/2024/8", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 감사한/배운 일 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentStats.class))),
            @ApiResponse(responseCode = "204", description = "해당 월에 작성한 감사한/배운 일 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 2개 이상 조회 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "감사한/배운 일 통계 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContents(@PathVariable("type") String type, @PathVariable("year") int year,
                                                 @PathVariable("month") int month,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearMonthPathParam(year, month);
            ContentType conType = ContentType.fromType(type);
            Long loginId = loginInfo.getMemberId();
            MonthlyContentStats result = statsService.getMonthlyContentStats(loginId, conType, year, month);
            if (isNoResult(result)) {
                return new ResponseEntity<>("해당 월에 작성한 감사한/배운 일 없음", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ErrorToSearchStatsException e) {
            log.error("통계 결과 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            log.error("통계 결과가 2개 이상 조회 - 409\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            log.error("작성한 감사한/배운 일 통계 정보 조회 실패 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 한 주 동안 일기 요약 통계 조회 */
    @GetMapping("/weekly/{year}/{week}")
    @Operation(summary = "주간 일기 요약 통계 조회", description = "로그인한 계정의 해당 년도/주차 일기 작성 횟수, 평균 점수, 일간 점수", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/weekly/2024/52", required = true, in = ParameterIn.PATH),
            @Parameter(name = "week", description = "조회할 주차", example = "/stats/weekly/2024/30", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주간 일기 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = WeeklyDairyStats.class))),
            @ApiResponse(responseCode = "204", description = "해당 주차에 작성한 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 2개 이상 조회 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "작성한 일기 통계 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendWeeklyStats(@PathVariable("year") int year, @PathVariable("week") int week,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearWeekPathParam(year, week);
            Long loginId = loginInfo.getMemberId();
            WeeklyDairyStats result = statsService.getWeeklyDairyStats(loginId, year, week);
            if (isNoResult(result)) {
                return new ResponseEntity<>("해당 주차에 작성한 일기 없음", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ErrorToSearchStatsException e) {
            log.error("통계 결과 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            log.error("통계 결과가 2개 이상 조회 - 409 \n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            log.error("작성한 일기 통계 정보 조회 실패 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 컨디션 세부 통계 조회 */
    @GetMapping("/monthly/condition/{year}/{month}")
    @Operation(summary = "한 달간 가장 많이 선택한 컨디션 상위 3개 조회", description = "로그인한 계정의 해당 년도/월 컨디션 기록 횟수, 가장 많은 비율로 선택된 컨디션 최대 3개 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/condition/2024/4", required = true, in = ParameterIn.PATH),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/condition/2024/10", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "한 달간 가장 많이 선택한 컨디션 상위 3개 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentStats.class))),
            @ApiResponse(responseCode = "204", description = "해당 월에 선택한 컨디션 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 통계 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyConditionRatioList(@PathVariable("year") int year, @PathVariable("month") int month,
                                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearMonthPathParam(year, month);
            Long loginId = loginInfo.getMemberId();
            MonthlyConditionsStats result = statsService.getMonthlyConditionStats(loginId, year, month);
            if (isNoResult(result)) {
                return new ResponseEntity<>("해당 월에 선택한 컨디션 없음", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ErrorToSearchStatsException e) {
            log.error("통계 결과 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            log.error("선택한 컨디션 통계 정보 조회 실패 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 감사한/배운 일 세부 통계 조회 */
    @GetMapping("/monthly/detail/{type}/{year}/{month}/{order_by}")
    @Operation(summary = "한 달간 작성한 모든 감사한/배운 일 조회", description = "로그인한 계정의 해당 년도/월 감사한/배운 일 작성 횟수, 작성한 모든 감사한/배운 일 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "감사한(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks/2024/10/date", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/detail/thanks/2024/2/high", required = true, in = ParameterIn.PATH),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/detail/learn/2024/5/low", required = true, in = ParameterIn.PATH),
            @Parameter(name = "order_by", description = "조회 순서 1.날짜순(date) 2.높은 점수순(high) 3.낮은 점수순(low)", example = "/stats/monthly/detail/thanks/2024/9/date", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "한 달간 작성한 모든 감사한/배운 일 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentStats.class))),
            @ApiResponse(responseCode = "204", description = "해당 년도/월에 작성한 감사한/배울 일이 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContentsList(@PathVariable("type") String type, @PathVariable("year") int year,
                                                     @PathVariable("month") int month, @PathVariable("order_by") String orderBy,
                                                     @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearMonthPathParam(year, month);
            ContentType conType = ContentType.fromType(type);
            SortType sortType = SortType.fromType(orderBy);
            Long loginId = loginInfo.getMemberId();
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(loginId, conType, year, month, sortType);
            if (isNoResult(result)) {
                return new ResponseEntity<>("해당 월에 작성한 감사한/배운 일 없음", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ErrorToSearchStatsException e) {
            log.error("통계 결과 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            log.error("작성한 감사한/배운 일 세부 정보 조회 실패 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean ifNotEnoughDairyForStats(MonthlyDairyStats result) {
        return result.getCount() < 3;
    }

    private static boolean isNoResult(StatsResponse result) {
        return result.getCount() < 1;
    }
}
