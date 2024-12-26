package com.soothee.stats.controller;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.custom.error.BindingErrorResult;
import com.soothee.custom.error.BindingErrorUtil;
import com.soothee.custom.exception.*;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.stats.dto.*;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Stats API", description = "월간/주간 동안 통계 조회")
@Slf4j
@RequestMapping("/stats")
public class StatsController {
    private final MemberService memberService;
    private final StatsService statsService;
    private final BindingErrorUtil bindingErrorUtil;

    /** 월간 일기 요약 통계 조회 */
    @GetMapping("/monthly")
    @Operation(summary = "월간 일기 요약 통계 조회", description = "로그인한 계정의 해당 년도/월 일기 작성 횟수, 평균 점수, 가장 많이 선택한 컨디션에 대한 정보 조회 - 통계 가능 최소 일기 작성 개수 3개", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 일기 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 일기가 통계 가능 최소 일기 작성 수(3)보다 적어 통계 불가", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 중복", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyStats(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || month query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 월간 일기 요약 통계 조회 */
            MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(memberId, monthParam);

            if (result.getCount() < 3) {
                /* 작성한 일기가 통계 가능 최소 일기 작성 개수 보다 적은 경우 통계 불가 - 204 : 오류는 아님 */
                return new ResponseEntity<>("해당 년도/월에 작성 일기 개수가 통계 가능 최소 일기 작성 개수를 만족하지 않음", HttpStatus.NO_CONTENT);
            }

            /* 성공 - 200 */
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (NullValueException | IncorrectValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ErrorToSearchStatsException e) {
            /* 통계 결과 조회 실패 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            /* 해당 조건으로 통계 결과가 1개 초과로 중복된 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 정보가 존재하지만 세부 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 감사한/배운 일 요약 통계 조회 */
    @GetMapping("/monthly/{type}")
    @Operation(summary = "월간 감사한/배운 일 요약 통계 조회", description = "로그인한 계정의 해당 년도/월 감사한/배운 일 작성 횟수, 가장 높은/낮은 점수의 감사한/배운 일 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "감사한(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/thanks?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/learn?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 감사한/배운 일 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 감사한/배운 일이 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 중복", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContents(@PathVariable("type") String type,
                                                 @ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || month query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        try {
            /* type Path Parameter String to Enum and validation */
            ContentType conType = ContentType.fromType(type);
            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);
            /* 월간 감사한/배운 일 요약 통계 조회 */
            MonthlyContentsDTO result = statsService.getMonthlyContents(memberId, conType, monthParam);

            if (result.getCount() < 1) {
                /* 작성한 감사한/배운 일이 없는 경우 - 204 : 오류는 아님 */
                return new ResponseEntity<>("해당 년도/월에 작성한 감사한/배운 일 없음", HttpStatus.NO_CONTENT);
            }

            /* 성공 - 200 */
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException | IncorrectParameterException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ErrorToSearchStatsException e) {
            /* 통계 결과 조회 실패 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            /* 해당 조건으로 통계 결과가 1개 초과로 중복된 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 정보가 존재하지만 세부 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 한 주 동안 일기 요약 통계 조회 */
    @GetMapping("/weekly")
    @Operation(summary = "주간 일기 요약 통계 조회", description = "로그인한 계정의 해당 년도/주차 일기 작성 횟수, 평균 점수, 일간 점수", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/weekly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "week", description = "조회할 주차", example = "/stats/weekly?year=2024&week=30", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주간 일기 요약 통계 조회 성공", content = @Content(schema = @Schema(implementation = WeeklyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "해당 년도/주차에 작성한 일기가 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "통계 결과 중복", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendWeeklyStats(@ModelAttribute @Valid WeekParam weekParam, BindingResult bindingResult,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || week query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 주간 일기 요약 통계 조회 */
            WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(memberId, weekParam);

            if (result.getCount() < 1) {
                /* 작성한 일기가 일기가 없는 경우 - 204 : 오류는 아님 */
                return new ResponseEntity<>("해당 년도/주차에 작성한 일기 없음", HttpStatus.NO_CONTENT);
            }

            /* 성공 - 200 */
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ErrorToSearchStatsException e) {
            /* 통계 결과 조회 실패 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            /* 해당 조건으로 통계 결과가 1개 초과로 중복된 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 정보가 존재하지만 세부 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 컨디션 세부 통계 조회 */
    @GetMapping("/monthly/condition")
    @Operation(summary = "월간 컨디션 세부 통계 조회", description = "로그인한 계정의 해당 년도/월 컨디션 기록 횟수, 가장 많은 비율로 선택된 컨디션 최대 3개 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/condition?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/condition?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 컨디션 세부 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "해당 년도/월에 선택한 컨디션이 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyConditionRatioList(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || month query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 한 달 동안 선택 횟수 상위 최대 3개 컨디션 리스트 조회 */
            MonthlyConditionsDTO result = statsService.getMonthlyConditionList(memberId, monthParam);
            if (result.getCount() < 1) {
                /* 선택한 컨디션이 없는 경우 - 204 : 오류는 아님 */
                return new ResponseEntity<>("해당 년도/월에 선택한 컨디션 없음", HttpStatus.NO_CONTENT);
            }

            /* 성공 - 200 */
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ErrorToSearchStatsException e) {
            /* 통계 결과 조회 실패 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 정보가 존재하지만 세부 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 월간 감사한/배운 일 세부 통계 조회 */
    @GetMapping("/monthly/detail/{type}")
    @Operation(summary = "월간 감사한/배운 일 세부 통계 조회", description = "로그인한 계정의 해당 년도/월 감사한/배운 일 작성 횟수, 작성한 모든 감사한/배운 일 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "감사한(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/detail/thanks?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/detail/learn?year=2024&month=10", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "order_by", description = "조회 순서 1.날짜순(date) 2.높은 점수순(high) 3.낮은 점수순(low)", example = "/stats/monthly/detail/thanks?year=2024&month=10&order_by=date", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월간 감사한/배운 일 세부 통계 조회 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "해당 년도/월에 작성한 감사한/배울 일이 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "통계 결과 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "통계 관련 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContentsList(@PathVariable("type") String type,
                                                     @ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                     @RequestParam("order_by") String orderBy,
                                                     @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || month query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* type Path Parameter String to ContentType and validation */
            ContentType conType = ContentType.fromType(type);
            /* orderBy Query Parameter String to SortType and validation */
            SortType sortType = SortType.fromType(orderBy);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 한 달 동안 작성한 모든 감사한/배운 일 리스트 Sorting 조회 */
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(memberId, conType, monthParam, sortType);

            if (result.getCount() < 1) {
                /* 작성한 감사한/배운 일이 없는 경우 - 204 : 오류는 아님 */
                return new ResponseEntity<>("해당 년도/월에 작성한 감사한/배운 일 없음", HttpStatus.NO_CONTENT);
            }

            /* 성공 - 200 */
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IncorrectParameterException | NullValueException | IncorrectValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ErrorToSearchStatsException e) {
            /* 통계 결과 조회 실패 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 정보가 존재하지만 세부 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
