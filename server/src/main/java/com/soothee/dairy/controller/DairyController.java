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
import com.soothee.dairy.service.command.DairyModify;
import com.soothee.dairy.service.command.DairyRegister;
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
            @ApiResponse(responseCode = "200", description = "한 달간 작성된 모든 일기 점수 조회 성공", content = @Content(schema = @Schema(implementation = DairyScoresDTO.class))),
            @ApiResponse(responseCode = "204", description = "로그인한 계정이 년도/월에 작성한 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendAllDairyMonthly(@ModelAttribute @Valid MonthParam monthParam, BindingResult bindingResult,
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

            /* 현재 로그인한 계정이 지정된 년도/월에 작성한 모든 일기의 작성 날짜와 오늘의 점수 리스트 조회 */
            List<DairyScoresDTO> result = dairyService.getAllDairyMonthly(memberId, monthParam);

            if (result.isEmpty()) {
                /* 로그인한 계정이 조회한 월에 작성한 일기가 없는 경우 - 204  */
                return new ResponseEntity<>("해당 년도/월에 작성한 일기 없음", HttpStatus.NO_CONTENT);
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
        }
    }

    /** 일기 조회 - 날짜로 */
    @GetMapping
    @Operation(summary = "지정된 날짜로 일기 상세 정보 조회", description = "로그인한 계정이 지정한 날에 작성한 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "조회할 날", example = "/dairy/calendar?date=2024-12-12", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 날짜의 일기 정보 조회 성공", content = @Content(schema = @Schema(implementation = DairyDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 날짜로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "조회한 작성 날짜에 등록한 일기가 1개 초과임", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
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
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotExistDairyException e) {
            /* 해당 작성 날짜로 조회한 로그인한 계정의 작성 일기가 없는 경우 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            /* 로그인한 계정이 해당 작성 날짜에 등록된 일기가 1개 초과인 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 일기 조회 - 일기 일련번호로 */
    @GetMapping("/{dairy_id}")
    @Operation(summary = "지정된 일기 일련번호로 일기 상세 정보 조회", description = "로그인한 계정이 작성한 해당 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "조회할 날", example = "/dairy/calendar/{dairy_id}", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 일련번호에 해당하는 일기 정보 조회 성공", content = @Content(schema = @Schema(implementation = DairyDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 일기 일련번호로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "조회한 작성 날짜에 등록한 일기가 1개 초과임", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
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
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotExistDairyException e) {
            /* 해당 일기 일련번호로 조회한 로그인한 계정의 작성 일기가 없는 경우 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            /* 로그인한 계정의 해당 일기 일련번호로 등록한 일기가 1개 초과인 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 일기 등록 */
    @PostMapping
    @Operation(summary = "새 일기 등록", description = "로그인한 계정이 작성한 새 특정일자 일기 등록", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "해당 날짜", example = "date=2024-10-11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "weather_id", description = "날씨 일련번호", example = "weather_id=WEAT001", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "score", description = "오늘의 점수", example = "score=3.2", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "cond_id_list", description = "다중 선택한 컨디션 일련번호 리스트", example = "cond_id_list=[1,2,4,5,3]", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "오늘의 요약", example = "content=개발을했다", in = ParameterIn.QUERY),
            @Parameter(name = "hope", description = "바랐던 방향성", example = "hope=놀고싶다", in = ParameterIn.QUERY),
            @Parameter(name = "thank", description = "감사한 일", example = "thank=점심을먹었다", in = ParameterIn.QUERY),
            @Parameter(name = "learn", description = "배운 일", example = "learn=사회는액팅이다", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "새 일기 등록 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "입력한 새 일기의 작성 날짜로 작성된 일기가 이미 존재함", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> registerDairy(@ModelAttribute @Valid DairyRegisterRequest param, BindingResult bindingResult,
                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* diary register dto query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            Long loginId = loginInfo.getMemberId();
            DairyRegister dairyInfo = DairyRegister.fromParam(loginId, param);
            dairyService.registerDairy(dairyInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullValueException e) {

            /* 성공 - 200 */
            return new ResponseEntity<>("성공", HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (DuplicatedResultException e) {
            /* 입력한 새 일기의 작성 날짜에 이미 등록된 일기가 있는 경우 - 409 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /** 일기 수정 */
    @PutMapping("/{dairy_id}")
    @Operation(summary = "일기 수정", description = "로그인한 계정이 작성한 특정 일자 일기 수정", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "일기 일련번호 || path의 일련번호와 query의 일련번호가 다르면 수정 불가", example = "/dairy/1111", required = true, in = ParameterIn.PATH),
            @Parameter(name = "dairy_id", description = "일기 일련번호 || path의 일련번호와 query의 일련번호가 다르면 수정 불가", example = "dairy_id=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "date", description = "해당 날짜 || 기존 dairy의 date와 query의 date가 다르면 수정 불가", example = "date=2024-10-11", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "weather_id", description = "날씨 일련번호", example = "weather_id=WEAT011", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "score", description = "오늘의 점수", example = "score=3.2", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "cond_id_list", description = "다중 선택한 컨디션 일련번호 리스트", example = "cond_id_list=[1,2,4,5,3]", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "오늘의 요약", example = "content=개발을했다", in = ParameterIn.QUERY),
            @Parameter(name = "hope", description = "바랐던 방향성", example = "hope=놀고싶다", in = ParameterIn.QUERY),
            @Parameter(name = "thank", description = "감사한 일", example = "thank=점심을먹었다", in = ParameterIn.QUERY),
            @Parameter(name = "learn", description = "배운 일", example = "learn=사회는액팅이다", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기존 일기 수정 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 일기 일련번호로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> modifyDairy(@PathVariable("dairy_id") Long dairyId,
                                         @ModelAttribute @Valid DairyModifyRequest param, BindingResult bindingResult,
                                         @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* dairy modify dto parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* path parameter validation */
            Long loginId = loginInfo.getMemberId();
            DairyModify dairyInfo = DairyModify.fromParam(loginId, param);
            dairyService.modifyDairy(dairyId, dairyInfo);
            return new ResponseEntity<>(HttpStatus.OK);

            /* 기존 일기 수정 */
            dairyService.modifyDairy(memberId, dairyId, inputInfo);

            /* 성공 - 200 */
            return new ResponseEntity<>("성공", HttpStatus.OK);

        } catch (IncorrectValueException | NullValueException | NotMatchedException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotExistDairyException e) {
            /* 해당 일기 일련번호로 조회한 로그인한 계정의 작성 일기가 없는 경우 - 404 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 일기 삭제 */
    @DeleteMapping("/{dairy_id}")
    @Operation(summary = "일기 삭제", description = "로그인한 계정이 작성한 특정 일자 일기 삭제", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "삭제할 일기 일련번호", example = "/dairy/1111", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기존 일기 삭제 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 일기 일련번호로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
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
        } catch (NotMatchedException | NullValueException | IncorrectValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotExistDairyException e) {
            /* 해당 일기 일련번호의 일기가 없는 경우 - 404 : 오류 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDetailInfoException e) {
            /* 해당 일기에 선택한 컨디션이 있지만 정보를 불러오지 못한 경우 - 500 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
