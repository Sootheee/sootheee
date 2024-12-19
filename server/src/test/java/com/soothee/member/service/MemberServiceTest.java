package com.soothee.member.service;

import com.soothee.config.TestConfig;
import com.soothee.member.dto.MemberDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Import(TestConfig.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    @DisplayName("OAuth2 로그인 시, 받은 정보로 회원 조회")
    void getMemberForOAuth2() {
        //given
        //when
        Member savedMember = memberService.getMemberForOAuth2(CommonTestCode.OAUTH2, CommonTestCode.SNS_TYPE).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

    @Test
    @DisplayName("회원 가입")
    void saveMember() {
        //given
        Member newMember = commonTestCode.getNewMember();
        //when
        memberService.saveMember(newMember);
        //then
        Assertions.assertThat(newMember.getOauth2ClientId()).isEqualTo("222222");
    }

    @Test
    @DisplayName("회원 닉네임 수정")
    void updateName() {
        //given
        String newName = "새 이름";
        Member newMember = commonTestCode.saveNewMember();
        //when
        newMember.updateName(newName);
        //then
        Assertions.assertThat(newMember.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("회원 다크모드 수정")
    void updateDarkMode() {
        //given
        String isDarkY = "Y";
        Member savedMember = commonTestCode.getSavedMember();
        //when
        savedMember.updateDarkModeYN(isDarkY);
        //then
        Assertions.assertThat(savedMember.getIsDark()).isEqualTo(isDarkY);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() {
        //given
        Member newMember = commonTestCode.saveNewMember();
        //when
        newMember.deleteMember();
        //then
        Assertions.assertThat(newMember.getIsDelete()).isEqualTo("Y");
    }

    @Test
    @DisplayName("로그인한 회원의 모든 정보 조회")
    void getAllMemberInfo() {
        //given
        //when
        MemberInfoDTO savedMember = memberService.getAllMemberInfo(CommonTestCode.MEMBER_ID);
        //then
        Assertions.assertThat(savedMember.getEmail()).isEqualTo("abc@def.com");
    }

    @Test
    @DisplayName("로그인한 회원의 닉네임만 조회")
    void getNicknameInfo() {
        //given
        //when
        MemberNameDTO savedMember = memberService.getNicknameInfo(CommonTestCode.MEMBER_ID);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

    @Test
    void getLoginMemberId() {
        //given
        //when
        //then
    }

    @Test
    void getLoginMember() {
        //given
        //when
        //then
    }

    @Test
    void getMemberById() {
        //given
        //when
        Member savedMember = memberService.getMemberById(CommonTestCode.MEMBER_ID);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

}