package com.soothee.member.controller;

import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import io.micrometer.common.util.StringUtils;
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
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관련 처리")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /** 회원 정보 전송 */
    @GetMapping("/info")
    @Operation(summary = "회원 정보", description = "로그인한 계정 정보로 필요한 정보를 전달", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "type", description = "닉네임만 조회할 때 사용 || 없으면 회원의 모든 정보 조회함", example = "/member/info?type=name", required = false, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = MemberInfoDTO.class))),
            @ApiResponse(responseCode = "206", description = "요청 성공", content = @Content(schema = @Schema(implementation = MemberNameDTO.class))),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> sendMemberInfo(@RequestParam(value = "type", required = false) String type,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        if (StringUtils.isNotBlank(type)) {
            MemberNameDTO result = memberService.getNicknameInfo(loginInfo);
            return new ResponseEntity<MemberNameDTO>(result, HttpStatus.PARTIAL_CONTENT);
        }
        MemberInfoDTO info = memberService.getAllMemberInfo(loginInfo);
        return new ResponseEntity<MemberInfoDTO>(info, HttpStatus.OK);
    }

    /** 다크모드 수정 */
    @PutMapping("/update/dark")
    @Operation(summary = "화면 보기 모드 수정", description = "회원의 다크모드 업데이트", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "memberId", description = "수정할 회원의 일련번호", example = "memberId=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isDark", description = "회원의 수정할 보기모드 || Y : dark mode / N : normal mode", example = "isDark=Y", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "로그인한 회원과 입력한 회원의 정보가 상이함", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateDarkMode(@RequestParam("memberId") Long memberId,
                                            @RequestParam("isDark") String isDark,
                                            @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        memberService.updateDarkMode(loginInfo, memberId, isDark);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /** 회원 정보(닉네임) 수정 */
    @PutMapping("/update/name")
    @Operation(summary = "회원 닉네임 수정", description = "회원이 입력한 닉네임으로 업데이트", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters({
            @Parameter(name = "memberId", description = "회원 고유일련번호", example = "memberId=1112", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "name", description = "회원이 입력한 새로운 닉네임", example = "name=사용자", required = true, in = ParameterIn.QUERY),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "로그인한 회원과 입력한 회원의 정보가 상이함", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateName(@RequestParam("memberId") Long memberId,
                                          @RequestParam("name") String name,
                                          @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        memberService.updateName(loginInfo, memberId, name);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /** 회원 탈퇴 */
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시, 소프트 삭제, 따로 파라미터를 넣지 않아도 현재 로그인한 계정 정보를 이용함", security = @SecurityRequirement(name = "oauth2_auth"))
    @Parameters(value = {
            @Parameter(name = "memberId", description = "탈퇴할 회원 일련번호", example = "memberId=1111", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "reasonId", description = "회원의 탈퇴 사유 일련번호", example = "reasonId=1", required = true, in = ParameterIn.QUERY)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "로그인한 회원과 입력한 회원의 정보가 상이함", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "접근 오류", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteMember(@RequestParam("memberId") Long memberId,
                                          @RequestParam("reasonId") Long reasonId,
                                          @AuthenticationPrincipal AuthenticatedUser loginInfo) {
        memberService.deleteMember(loginInfo, memberId, reasonId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
