package com.soothee.dairy.controller;

import com.soothee.common.constants.DomainType;
import com.soothee.custom.error.BindingErrorResult;
import com.soothee.custom.error.BindingErrorUtil;
import com.soothee.custom.exception.*;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.dairy.service.DairyService;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Dairy API", description = "일기 관련 처리")
@Slf4j
@RequestMapping("/dairy")
public class DairyController {
    private final MemberService memberService;
    private final DairyService dairyService;
    private final BindingErrorUtil bindingErrorUtil;

    /** 달력(홈)에 표시될 해당 월의 모든 일기 점수 조회 */
    @GetMapping("/calendar")
    @Operation(summary = "달력의 출력될 일기들의 오늘의 점수 리스트 조회", description = "로그인한 계정이 지정한 달에 작성한 모든 일기 일련번호와 각 오늘의 점수 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/dairy/calendar?year=2022", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "month", description = "조회할 달", example = "/dairy/calendar?year=2022&month=2", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = DairyScoresDTO.class))),
            @ApiResponse(responseCode = "202", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendAllDairyMonthly(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* year || month query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        List<DairyScoresDTO> infos = dairyService.getAllDairyMonthly(memberId, monthParam);
        if (infos.isEmpty()) {
            return new ResponseEntity<String>(MyErrorMsg.NOT_ENOUGH_DAIRY_COUNT.toString(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<DairyScoresDTO>>(infos, HttpStatus.OK);
    }

    /** 일기 조회 - 날짜로 */
    @GetMapping
    @Operation(summary = "지정된 날짜로 일기 상세 정보 조회", description = "로그인한 계정이 지정한 날에 작성한 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "조회할 날", example = "/dairy/calendar?date=2024-12-12", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = DairyDTO.class))),
            @ApiResponse(responseCode = "202", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "조회된 일기 1 개 초과", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendDairyByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* date query parameter validation */
            SootheeValidation.checkDate(date);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 현재 로그인한 계정이 지정한 날짜에 작성한 고유한 하나의 일기 조회 */
            DairyDTO findDairy = dairyService.getDairyByDate(memberId, date);

            /* 성공 - 200 */
            return new ResponseEntity<>(findDairy, HttpStatus.OK);

        } catch (NullValueException | IncorrectValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /** 일기 조회 - 일기 일련번호로 */
    @GetMapping("/{dairy_id}")
    @Operation(summary = "지정된 일기 일련번호로 일기 상세 정보 조회", description = "로그인한 계정이 작성한 해당 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "조회할 날", example = "/dairy/calendar/{dairy_id}", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = DairyDTO.class))),
            @ApiResponse(responseCode = "202", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "조회된 일기 1 개 초과", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendDairyByDairyId(@PathVariable("dairy_id") Long dairyId,
                                                @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* dairy_id path parameter validation */
            SootheeValidation.checkDomainId(dairyId, DomainType.DAIRY);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 현재 로그인한 계정이 작성한 지정한 일기 일련번호를 가진 고유한 하나의 일기 조회 */
            DairyDTO findDairy = dairyService.getDairyByDairyId(memberId, dairyId);

            /* 성공 - 200 */
            return new ResponseEntity<>(findDairy, HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /** 일기 등록 */
    @PostMapping
    @Operation(summary = "새 일기 등록", description = "로그인한 계정이 작성한 새 특정일자 일기 등록", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "해당 날짜", example = "date=2024-10-11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "weather_id", description = "날씨 일련번호", example = "weatherId=11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "score", description = "오늘의 점수", example = "score=3.2", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "cond_ids", description = "다중 선택한 컨디션 일련번호 리스트", example = "condIds=[1,2,4,5,3]", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "오늘의 요약", example = "content=개발을했다", in = ParameterIn.QUERY),
            @Parameter(name = "hope", description = "바랐던 방향성", example = "hope=놀고싶다", in = ParameterIn.QUERY),
            @Parameter(name = "thank", description = "감사한 일", example = "thank=점심을먹었다", in = ParameterIn.QUERY),
            @Parameter(name = "learn", description = "배운 일", example = "learn=사회는액팅이다", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 오류 || 일기 중복 등록 시도 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> registerDairy(@ModelAttribute @Valid DairyRegisterDTO inputInfo, BindingResult bindingResult,
                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* diary register dto query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        dairyService.registerDairy(memberId, inputInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /** 일기 수정 */
    @PutMapping("/{dairy_id}")
    @Operation(summary = "일기 수정", description = "로그인한 계정이 작성한 특정 일자 일기 수정", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "일기 일련번호 || path의 일련번호와 query의 일련번호가 다르면 수정 불가", example = "/dairy/1111", required = true, in = ParameterIn.PATH),
            @Parameter(name = "dairy_id", description = "일기 일련번호 || path의 일련번호와 query의 일련번호가 다르면 수정 불가", example = "dairyId=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "date", description = "해당 날짜 || 기존 dairy의 date와 query의 date가 다르면 수정 불가", example = "date=2024-10-11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "weather_id", description = "날씨 일련번호", example = "weatherId=11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "score", description = "오늘의 점수", example = "score=3.2", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "cond_ids", description = "다중 선택한 컨디션 일련번호 리스트", example = "condIds=[1,2,4,5,3]", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "오늘의 요약", example = "content=개발을했다", in = ParameterIn.QUERY),
            @Parameter(name = "hope", description = "바랐던 방향성", example = "hope=놀고싶다", in = ParameterIn.QUERY),
            @Parameter(name = "thank", description = "감사한 일", example = "thank=점심을먹었다", in = ParameterIn.QUERY),
            @Parameter(name = "learn", description = "배운 일", example = "learn=사회는액팅이다", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "204", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 오류 || 주요 값이 일치하지 않음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> modifyDairy(@PathVariable("dairy_id") Long dairyId,
                                         @ModelAttribute @Valid DairyDTO inputInfo, BindingResult bindingResult,
                                         @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* dairy modify dto parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* path parameter validation */
            SootheeValidation.checkDomainId(dairyId, DomainType.DAIRY);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 기존 일기 수정 */
            dairyService.modifyDairy(memberId, dairyId, inputInfo);

            /* 성공 - 200 */
            return new ResponseEntity<>("성공", HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException | NotMatchedException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /** 일기 삭제 */
    @DeleteMapping("/{dairy_id}")
    @Operation(summary = "일기 삭제", description = "로그인한 계정이 작성한 특정 일자 일기 삭제", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "삭제할 일기 일련번호", example = "/dairy/1111", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "204", description = "조회된 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "일기 접근 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteDairy(@PathVariable("dairy_id") Long dairyId,
                                         @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* path parameter validation */
            SootheeValidation.checkDomainId(dairyId, DomainType.DAIRY);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* 작성된 일기 삭제 */
            dairyService.deleteDairy(memberId, dairyId);

            /* 성공 - 200 */
            return new ResponseEntity<>("성공", HttpStatus.OK);
        }
    }
}
