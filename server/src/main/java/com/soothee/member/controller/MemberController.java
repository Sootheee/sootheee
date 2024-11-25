package com.soothee.member.controller;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.UpdateMemberDTO;
import com.soothee.member.service.MemberService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관련 처리")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/update")
    @Operation(summary = "회원의 현재 닉네임", description = "회원 닉네임 변경 세팅 창에서 닉네임을 바꾸기 전에 현재 닉네임을 먼저 출력",
            security = @SecurityRequirement(name = "oauth2_auth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content(mediaType = "text/plain"))
    })
    @Parameters({
            @Parameter(name = "id", description = "회원 고유일련번호", example = "1112"),
            @Parameter(name = "member_name", description = "회원 닉네임", example = "사용자")
    })
    public ResponseEntity<?> updateMemberName(Principal principal) {
        String loginId = principal.getName();
        Member loginMember = memberService.getMemberByEmail(loginId);
        UpdateMemberDTO result = UpdateMemberDTO.builder()
                .id(loginMember.getId())
                .memberName(loginMember.getMemberName()).build();
        if (Objects.isNull(loginMember.getId()) || StringUtils.isBlank(loginMember.getMemberName())) {
            return new ResponseEntity<String>(MyErrorMsg.NULL_VALUE.toString(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UpdateMemberDTO>(result, HttpStatus.OK);
    }
}
