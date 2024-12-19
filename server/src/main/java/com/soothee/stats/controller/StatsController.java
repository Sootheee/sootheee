package com.soothee.stats.controller;

import com.soothee.common.exception.MyErrorMsg;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Tag(name = "Stats API", description = "월간/주간 통계 조회")
@RequestMapping("/stats")
public class StatsController {
    private final MemberService memberService;
    private final StatsService statsService;

    /** 통계 월간 요약 */
    @GetMapping("/monthly")
    @Operation(summary = "통계 월간 평균 요약", description = "로그인한 계정의 해당 달 일기 작성 횟수, 평균 점수, 최빈 컨디션에 대한 정보 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 일기 수 부족", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyStats(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        Long memberId = memberService.getLoginMemberId(loginInfo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<BindingResult>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(memberId, monthParam);
        if (result.getCount() < 3) {
            return new ResponseEntity<String>(MyErrorMsg.NOT_ENOUGH_DAIRY_COUNT.toString(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<MonthlyStatsDTO>(result, HttpStatus.OK);
    }

    /** 통계 월간 감사한/배운 일 요약 */
    @GetMapping("/monthly/{type}")
    @Operation(summary = "통계 월간 고마운/배운 일 요약", description = "로그인한 계정의 해당 달 고마운/배운 일 작성 횟수, 가장 높은/낮은 점수의 고마운/배운 일 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "고마운(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/thanks?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/learn?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 고마운/배운 일 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContents(@PathVariable("type") String type,
                                                 @ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        Long memberId = memberService.getLoginMemberId(loginInfo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<BindingResult>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        MonthlyContentsDTO result = statsService.getMonthlyContents(memberId, type, monthParam);
        if (result.getCount() < 1) {
            return new ResponseEntity<String>(MyErrorMsg.NO_CONTENTS.toString(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<MonthlyContentsDTO>(result, HttpStatus.OK);
    }

    /** 통계 주간 요약 */
    @GetMapping("/weekly")
    @Operation(summary = "통계 주간 평균 요약", description = "로그인한 계정의 해당 주차 일기 작성 횟수, 평균 점수, 일간 점수", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/weekly?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "week", description = "조회할 주차", example = "/stats/weekly?year=2024&week=30", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = WeeklyStatsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 일기 수 부족", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendWeeklyStats(@ModelAttribute @Valid WeekParam weekParam, BindingResult bindingResult,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        Long memberId = memberService.getLoginMemberId(loginInfo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<BindingResult>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(memberId, weekParam);
        if (result.getCount() < 3) {
            return new ResponseEntity<String>(MyErrorMsg.NOT_ENOUGH_DAIRY_COUNT.toString(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<WeeklyStatsDTO>(HttpStatus.OK);
    }

    /** 통계 월간 컨디션 세부 */
    @GetMapping("/monthly/condition")
    @Operation(summary = "통계 월간 컨디션 요약", description = "로그인한 계정의 해당 달 컨디션 기록 횟수, 가장 많은 비율로 선택된 컨디션 최대 3개 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/condition?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/condition?year=2024&month=10", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "기록한 일기 및 컨디션이 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyConditionRatioList(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        Long memberId = memberService.getLoginMemberId(loginInfo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<BindingResult>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        MonthlyConditionsDTO result = statsService.getMonthlyConditionList(memberId, monthParam);
        if (result.getCount() < 1) {
            return new ResponseEntity<String>(MyErrorMsg.NO_CONTENTS.toString(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<MonthlyConditionsDTO>(result, HttpStatus.OK);
    }

    /** 월간 작성한 모든 고마운/배운 일 리스트 조회 */
    @GetMapping("/monthly/detail/{type}")
    @Operation(summary = "월간 작성한 모든 고마운/배운 일 리스트", description = "로그인한 계정의 해당 달 고마운/배운 일 작성 횟수, 작성한 모든 고마운/배운 일 리스트 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "고마운(thanks)/배운(learn) 일 종류", example = "/stats/monthly/thanks", required = true, in = ParameterIn.PATH),
            @Parameter(name = "year", description = "조회할 년도", example = "/stats/monthly/detail/thanks?year=2024", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/stats/monthly/detail/learn?year=2024&month=10", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "order_by", description = "조회 순서", example = "/stats/monthly/detail/thanks?year=2024&month=10&order_by=date", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MonthlyContentsDTO.class))),
            @ApiResponse(responseCode = "204", description = "작성한 고마운/배운 일 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMonthlyContentsList(@PathVariable("type") String type,
                                                     @ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                     @RequestParam("order_by") String orderBy,
                                                     @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        Long memberId = memberService.getLoginMemberId(loginInfo);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<BindingResult>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<MonthlyAllContentsDTO>(HttpStatus.OK);
    }
}
