package com.soothee.member.controller;

import com.soothee.common.constants.BooleanYN;
import com.soothee.custom.error.BindingErrorResult;
import com.soothee.custom.exception.*;
import com.soothee.custom.error.BindingErrorUtil;
import com.soothee.member.controller.request.MemberDeleteRequest;
import com.soothee.member.controller.response.MemberAllInfoResponse;
import com.soothee.member.controller.response.MemberNameResponse;
import com.soothee.member.service.MemberService;
import com.soothee.member.service.command.MemberDelete;
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

import static com.soothee.custom.valid.SootheeValidation.*;

@Controller
@Tag(name = "Member API", description = "회원 관련 처리")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final BindingErrorUtil bindingErrorUtil;

    /** 회원 정보 전송 */
    @GetMapping("/info/{type}")
    @Operation(summary = "회원 정보", description = "로그인한 계정 정보로 필요한 정보 조회", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "값은 name만 가능, 닉네임만 조회할 때 사용\ntype 파라미터가 없으면 회원의 모든 정보 조회함", example = "/member/info/name", in = ParameterIn.PATH)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 전체 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberAllInfoResponse.class))),
            @ApiResponse(responseCode = "206", description = "회원 닉네임 정보 조회 성공", content = @Content(schema = @Schema(implementation = MemberNameResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "로그인한 회원 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMemberInfo(@PathVariable(value = "type", required = false) String type,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validTypeQueryParameter(type);
            Long loginId = loginInfo.getMemberId();
            if (isNoTypeQueryParameter(type)) {
                MemberAllInfoResponse result = memberService.getAllMemberInfo(loginId);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            MemberNameResponse result = memberService.getNicknameInfo(loginId);
            return new ResponseEntity<>(result, HttpStatus.PARTIAL_CONTENT);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            log.error("로그인한 회원 정보 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
            @ApiResponse(responseCode = "200", description = "회원 다크모드 업데이트 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "회원 다크모드 업데이트 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "로그인한 회원 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateDarkMode(@RequestParam("member_id") Long memberId,  @RequestParam("is_dark") String isDark,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validMemberIdPathParam(memberId);
            BooleanYN isDarkYN = BooleanYN.fromString(isDark);
            Long loginId = loginInfo.getMemberId();
            memberService.updateDarkMode(loginId, memberId, isDarkYN);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            /* 로그인한 인증된 계정의 정보를 조회하지 못한 경우 - 401 */
            log.error(e.getMessage());
        } catch (NotExistMemberException e){
            log.error("로그인한 회원 정보 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "회원 닉네임 업데이트 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "로그인한 회원 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateName(@RequestParam("member_id") Long memberId,
                                        @RequestParam("name") String name,
                                        @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        try {
            validMemberIdPathParam(memberId);
            validNameQueryParameter(name);
            Long loginId = loginInfo.getMemberId();
            memberService.updateName(loginId, memberId, name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IncorrectParameterException e) {
            log.error("요청 파라미터 값이 올바르지 않거나 없음 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            log.error("로그인한 회원 정보 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
            @ApiResponse(responseCode = "400", description = "요청 파라미터 값이 올바르지 않거나 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "회원 탈퇴 권한 없음", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "로그인한 회원 정보 조회 실패", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteMember(@ModelAttribute @Valid MemberDeleteRequest param, BindingResult bindingResult,
                                          @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        if (isNotValidRequestParameter(bindingResult)) {
            List<BindingErrorResult> errorResults = bindingErrorUtil.getErrorResponse(bindingResult);
            return new ResponseEntity<>(errorResults, HttpStatus.BAD_REQUEST);
        }
        try {
            Long loginId = loginInfo.getMemberId();
            MemberDelete deleteInfo = MemberDelete.fromParam(param);
            memberService.deleteMember(loginId, deleteInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullValueException e) {
            log.error("요청한 탈퇴 사유 일련번호로 조회 실패 - 400\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotExistMemberException e) {
            log.error("로그인한 회원 정보 조회 실패 - 404\n", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private static boolean isNotValidRequestParameter(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    private static boolean isNoTypeQueryParameter(String type) {
        return StringUtils.isBlank(type);
    }
}
