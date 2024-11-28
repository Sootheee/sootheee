package com.soothee.member.controller;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.service.GoogleOAuth2UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberController memberController;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private GoogleOAuth2UserService googleOAuth2UserService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("회원 정보 가져오기 성공")
    void sendMemberInfo() throws Exception {
        //given
        Member member = Member.builder()
                .email("abc@abc.com")
                .oauth2ClientId("1111")
                .snsType(SnsType.KAKAOTALK)
                .name("test")
                .build();
        memberService.saveMember(member);

        //when
        mockMvc.perform(get("/member/info")).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @Test
    void updateDarkMode() {
    }

    @Test
    void updateName() {
    }

    @Test
    void deleteMember() {
    }
}