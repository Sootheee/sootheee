package com.soothee.member.controller;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.DomainType;
import com.soothee.custom.error.BindingErrorResult;
import com.soothee.custom.exception.*;
import com.soothee.custom.error.BindingErrorUtil;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관련 처리")
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final BindingErrorUtil bindingErrorUtil;

    /** 회원 정보 전송 */
    @GetMapping("/info")
    @Operation(summary = "회원 정보", description = "로그인한 계정 정보로 필요한 정보 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "값은 name만 가능, 닉네임만 조회할 때 사용\ntype 파라미터가 없으면 회원의 모든 정보 조회함", example = "/member/info?type=name", in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 전체 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberInfoDTO.class))),
            @ApiResponse(responseCode = "206", description = "회원 닉네임 정보 요청 성공", content = @Content(schema = @Schema(implementation = MemberNameDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMemberInfo(@RequestParam(value = "type", required = false) String type,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* query parameter validation - null->true */
            SootheeValidation.checkTypeQueryParameter(type);

            /* 로그인한 계정 일련번호 조회 */
            Long memberId = memberService.getLoginMemberId(loginInfo);

            /* type Query Parameter 없으면 로그인한 계정의 모든 정보를 조회 */
            if (StringUtils.isBlank(type)) {
                /* 로그인한 계정의 모든 정보 조회 */
                MemberInfoDTO result = memberService.getAllMemberInfo(memberId);
                /* 성공 - 200 */
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            /* type Query Parameter 있으면 로그인한 계정의 회원 닉네임만 조회 */
            MemberNameDTO result = memberService.getNicknameInfo(memberId);
            /* 일부 값만 전송 성공 - 206 */
            return new ResponseEntity<>(result, HttpStatus.PARTIAL_CONTENT);

        } catch (IncorrectValueException | NullValueException | IncorrectParameterException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /** 다크모드 수정 */
    @PutMapping("/update/dark")
    @Operation(summary = "화면 보기 모드 수정", description = "회원의 다크모드 업데이트", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "member_id", description = "수정할 회원의 일련번호", example = "member_id=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "is_dark", description = "회원의 수정할 보기모드 || Y : dark mode / N : normal mode", example = "is_dark=Y", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 다크모드 변경 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateDarkMode(@RequestParam("member_id") Long memberId,
                                            @RequestParam("is_dark") String isDark,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* query parameter validation */
            SootheeValidation.checkDomainId(memberId, DomainType.MEMBER);
            /* query parameter validation || String to Enum */
            BooleanYN isDarkYN = BooleanYN.fromString(isDark);

            /* 로그인한 계정 일련번호 조회 */
            Long loginMemberId = memberService.getLoginMemberId(loginInfo);

            /* 다크모드 변경 */
            memberService.updateDarkMode(loginMemberId, memberId, isDarkYN);

            /* 성공 - 200 */
            return new ResponseEntity<>("성공", HttpStatus.OK);

        } catch (IncorrectValueException | NotMatchedException | NullValueException e) {
            /* 필수 요청 파라미터의 값이나 필수 응답값이 없거나 올바르지 않은 경우 - 400 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /** 회원 정보(닉네임) 수정 */
    @PutMapping("/update/name")
    @Operation(summary = "회원 닉네임 수정", description = "회원이 입력한 닉네임으로 업데이트", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters({
            @Parameter(name = "member_id", description = "회원 고유일련번호", example = "member_id=1112", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "name", description = "회원이 입력한 새로운 닉네임", example = "name=사용자", required = true, in = ParameterIn.QUERY),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 닉네임 수정 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateName(@RequestParam("member_id") Long memberId,
                                        @RequestParam("name") String name,
                                        @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            /* query parameter validation */
            SootheeValidation.checkDomainId(memberId, DomainType.MEMBER);
            SootheeValidation.checkQueryParameter(name);

            /* 로그인한 계정 일련번호 조회 */
            Long loginMemberId = memberService.getLoginMemberId(loginInfo);

            /* 회원 닉네임 수정 */
            memberService.updateName(loginMemberId, memberId, name);

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
        }
    }

    /** 회원 탈퇴 */
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시, 소프트 삭제, 따로 파라미터를 넣지 않아도 현재 로그인한 계정 정보를 이용함", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "member_id", description = "탈퇴할 회원 일련번호", example = "member_id=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "del_reason_id_list", description = "회원의 탈퇴 사유 일련번호 리스트", example = "del_reason_id_list=[1,2,3,4]", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "필수 요청 파라미터 값이나 필수 응답값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "로그인한 인증된 계정의 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteMember(@ModelAttribute @Valid MemberDelDTO memberDelDTO, BindingResult bindingResult,
                                          @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        /* member_id || del_reason_list query parameter validation */
        if (bindingResult.hasErrors()) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            /* 필수 요청 파라미터의 값이 없거나 올바르지 않은 경우 - 400 */
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }

        try {
            /* 로그인한 계정 일련번호 조회 */
            Long loginMemberId = memberService.getLoginMemberId(loginInfo);

            /* 회원 소프트 삭제 */
            memberService.deleteMember(loginMemberId, memberDelDTO);

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
        }
    }
}
