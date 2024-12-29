package com.soothee.dairy.controller;

import com.soothee.custom.error.BindingErrorResult;
import com.soothee.custom.error.BindingErrorUtil;
import com.soothee.custom.exception.*;
import com.soothee.dairy.controller.request.DairyModifyRequest;
import com.soothee.dairy.controller.response.DairyAllResponse;
import com.soothee.dairy.controller.request.DairyRegisterRequest;
import com.soothee.dairy.controller.response.DairyScoresResponse;
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

import static com.soothee.custom.valid.SootheeValidation.*;

@Controller
@RequiredArgsConstructor
@Tag(name = "Dairy API", description = "일기 관련 처리")
@Slf4j
@RequestMapping("/dairy")
public class DairyController {
    private final DairyService dairyService;
    private final BindingErrorUtil bindingErrorUtil;

    /** 달력(홈)에 표시될 해당 월의 모든 일기 점수 조회 */
    @GetMapping("/calendar/{year}/{month}")
    @Operation(summary = "달력의 출력될 일기들의 오늘의 점수 리스트 조회", description = "로그인한 계정이 조회할 달에 작성한 모든 일기 일련번호와 각 오늘의 점수 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "year", description = "조회할 년도", example = "/dairy/calendar/2024/10", required = true, in = ParameterIn.PATH),
            @Parameter(name = "month", description = "조회할 달", example = "/dairy/calendar/2024/2", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "한 달간 작성한 일기 조회 성공", content = @Content(schema = @Schema(implementation = DairyScoresResponse.class))),
            @ApiResponse(responseCode = "204", description = "해당 월에 작성한 일기 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendAllDairyMonthly(@PathVariable("year") int year, @PathVariable("month") int month,
                                                 @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validYearMonthPathParam(year, month);
            Long loginId = loginInfo.getMemberId();
            List<DairyScoresResponse> result = dairyService.getAllDairyMonthly(loginId, year, month);
            if (isNoResult(result)) {
                return new ResponseEntity<>("해당 월에 작성한 일기 없음", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /** 일기 조회 - 날짜로 */
    @GetMapping("/{date}")
    @Operation(summary = "조회할 날짜로 일기 상세 정보 조회", description = "로그인한 계정이 조회할 날에 작성한 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "date", description = "조회할 날", example = "/dairy/calendar/2024-12-12", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 날짜로 일기 조회 성공", content = @Content(schema = @Schema(implementation = DairyAllResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 날짜로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "조회한 작성 날짜에 등록한 일기가 2개 이상임", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendDairyByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                             @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validDatePathParam(date);
            Long loginId = loginInfo.getMemberId();
            DairyAllResponse findDairy = dairyService.getDairyByDate(loginId, date);
            return new ResponseEntity<>(findDairy, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoDairyResultException e) {
            log.error("해당 작성 날짜로 조회한 로그인한 계정의 작성 일기가 없는 경우 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            log.error("로그인한 계정이 해당 작성 날짜에 등록된 일기가 2개 이상인 경우 - 409\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDairyConditionsException e) {
            log.error("일기의 선택한 컨디션 세부 정보를 불러오지 못한 경우 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 일기 조회 - 일기 일련번호로 */
    @GetMapping("/{dairy_id}")
    @Operation(summary = "조회할 일기 일련번호로 일기 상세 정보 조회", description = "로그인한 계정이 작성한 해당 일기 모든 정보 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "dairy_id", description = "조회할 날", example = "/dairy/calendar/111111", required = true, in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 일련번호로 일기 조회 성공", content = @Content(schema = @Schema(implementation = DairyAllResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "조회한 일기 일련번호로 작성된 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "조회한 작성 날짜에 등록한 일기가 2개 이상임", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendDairyByDairyId(@PathVariable("dairy_id") Long dairyId,
                                                @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validDairyIdPathParam(dairyId);
            Long loginId = loginInfo.getMemberId();
            DairyAllResponse findDairy = dairyService.getDairyByDairyId(loginId, dairyId);
            return new ResponseEntity<>(findDairy, HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoDairyResultException e) {
            log.error("해당 일기 일련번호로 조회한 로그인한 계정의 작성 일기가 없는 경우 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicatedResultException e) {
            log.error("로그인한 계정의 해당 일기 일련번호로 등록한 일기가 2개 이상인 경우 - 409\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundDairyConditionsException e) {
            log.error("일기의 선택한 컨디션 세부 정보를 불러오지 못한 경우 - 500\n", e);
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
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "입력한 새 일기의 작성 날짜로 작성된 일기가 이미 존재함", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> registerDairy(@ModelAttribute @Valid DairyRegisterRequest param, BindingResult bindingResult,
                                           @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        if (isNotValidRequestParameter(bindingResult)) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        try {
            Long loginId = loginInfo.getMemberId();
            DairyRegister dairyInfo = DairyRegister.fromParam(loginId, param);
            dairyService.registerDairy(dairyInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullValueException e) {
            log.error("요청한 날씨/컨디션 일련번호로 조회 실패 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            log.error("로그인한 회원의 인증/인가 오류 - 403\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (DuplicatedResultException e) {
            log.error("입력한 새 일기의 작성 날짜에 이미 등록된 일기가 있는 경우 - 409\n", e);
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
            @ApiResponse(responseCode = "200", description = "일기 수정 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "일기 수정 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "수정할 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> modifyDairy(@PathVariable("dairy_id") Long dairyId,
                                         @ModelAttribute @Valid DairyModifyRequest param, BindingResult bindingResult,
                                         @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        if (isNotValidRequestParameter(bindingResult)) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        try {
            validDairyIdPathParam(dairyId);
            Long loginId = loginInfo.getMemberId();
            DairyModify dairyInfo = DairyModify.fromParam(loginId, param);
            dairyService.modifyDairy(dairyId, dairyInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("필수 요청 파라미터의 값이 올바르지 않은 경우 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NullValueException e) {
            log.error("요청한 날씨/컨디션 일련번호로 조회 실패 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotMatchedException e) {
            log.error("수정 요청 일기 일련번호/작성 일자가 기존 일기의 정보와 일치하지 않는 경우 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoAuthorizeException e) {
            log.error("현재 로그인한 회원에게 해당 일기의 수정 권한이 없는 경우 - 403\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoDairyResultException e) {
            log.error("수정할 일기 조회 실패한 경우 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDairyConditionsException e) {
            log.error("기존/수정 일기의 선택한 컨디션 정보를 불러오지 못한 경우 - 500\n", e);
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
            @ApiResponse(responseCode = "200", description = "일기 삭제 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "일기 수정 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "삭제할 일기 조회 실패", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "선택한 컨디션 상세 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteDairy(@PathVariable("dairy_id") Long dairyId,
                                         @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validDairyIdPathParam(dairyId);
            Long loginId = loginInfo.getMemberId();
            dairyService.deleteDairy(loginId, dairyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoAuthorizeException e) {
            log.error("현재 로그인한 회원에게 해당 일기의 수정 권한이 없는 경우 - 403\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoDairyResultException e) {
            log.error("삭제할 일기 조회 실패한 경우 - 404 : 오류\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotFoundDairyConditionsException e) {
            log.error("일기의 선택한 컨디션 세부 정보를 불러오지 못한 경우 - 500\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 요청 파라미터 valid 결과 Error 존재 여부
     * 
     * @param bindingResult valid 결과
     * @return Error 있으면 true / 없으면 false
     */
    private static boolean isNotValidRequestParameter(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    /**
     * 결과가 없는 경우
     * 
     * @param result valid 할 결과 리스트
     * @return 결과가 없으면 true / 있으면 false
     */
    private static boolean isNoResult(List<?> result) {
        return result.isEmpty();
    }
}
