package com.soothee.member.controller;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.UpdateMemberDTO;
import com.soothee.member.service.MemberService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관련 처리")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /** 회원 수정 페이지 조회 시, 필요한 정보 */
    @GetMapping("/update")
    @Operation(summary = "회원의 현재 닉네임", description = "회원 닉네임 변경 세팅 창에서 닉네임을 바꾸기 전에 현재 닉네임을 먼저 출력, 따로 파라미터를 넣지 않아도 현재 로그인한 계정 정보를 이용함",
            security = @SecurityRequirement(name = "oauth2_auth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "회원 정보 없음", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateMemberName(Principal principal) {
        Member loginMember = memberService.getLoginMember(principal);
        UpdateMemberDTO result = UpdateMemberDTO.builder()
                                                .id(loginMember.getId())
                                                .memberName(loginMember.getMemberName()).build();
        return new ResponseEntity<UpdateMemberDTO>(result, HttpStatus.OK);
    }

    /** 회원 정보(닉네임) 수정 */
    @PostMapping("/update")
    @Operation(summary = "회원 닉네임 수정", description = "회원이 입력한 닉네임으로 업데이트",
            security = @SecurityRequirement(name = "oauth2_auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "로그인한 회원과 입력한 회원의 정보가 상이함", content = @Content(mediaType = "text/plain"))
    })
    @Parameters({
            @Parameter(name = "id", description = "회원 고유일련번호", example = "1112"),
            @Parameter(name = "member_name", description = "회원이 입력한 새로운 닉네임", example = "사용자")
    })
    public ResponseEntity<?> updateMemberName(@ModelAttribute UpdateMemberDTO updateInfo, Principal principal) {
        Member loginMember = memberService.getLoginMember(principal);
        if(memberService.isNotLoginMemberInfo(loginMember, updateInfo)) {
            return new ResponseEntity<String>(MyErrorMsg.MISS_MATCH_MEMBER.toString(), HttpStatus.BAD_REQUEST);
        }
        memberService.updateMember(loginMember, updateInfo);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /** 회원 탈퇴 */
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시, 소프트 삭제, 따로 파라미터를 넣지 않아도 현재 로그인한 계정 정보를 이용함",
            security = @SecurityRequirement(name = "oauth2_auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteMember(Principal principal) {
        Member loginMember =  memberService.getLoginMember(principal);
        memberService.deleteMember(loginMember);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
