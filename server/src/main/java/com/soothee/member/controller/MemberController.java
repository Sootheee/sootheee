package com.soothee.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "Member API", description = "회원 관련 처리")
@RequestMapping("/member")
public class MemberController {
}
