package com.soothee.member.controller;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.UpdateMemberDTO;
import com.soothee.member.service.MemberService;
import io.micrometer.common.util.StringUtils;
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
