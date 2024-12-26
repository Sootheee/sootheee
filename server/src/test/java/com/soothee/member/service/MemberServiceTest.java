package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NotExistMemberException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        try {
            //given
            Member newMember = commonTestCode.getNewMember();
            //when
            memberService.saveMember(newMember);
            //then
            Assertions.assertThat(newMember.getOauth2ClientId()).isEqualTo("222222");
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("회원 닉네임 수정")
    void updateName() {
        try {
            //given
            String newName = "새 이름";
            Member newMember = commonTestCode.saveNewMember();
            //when
            try {
                newMember.updateName(newName);
            } catch (IncorrectValueException | NullValueException e) {
                log.error(e.getMessage());
            }
            //then
            Assertions.assertThat(newMember.getName()).isEqualTo(newName);
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("회원 다크모드 수정")
    void updateDarkMode() {
        //given
        BooleanYN isDark = BooleanYN.Y;
        Member savedMember = commonTestCode.getSavedMember();
        try {
            //when
            savedMember.updateDarkModeYN(isDark);
            //then
            Assertions.assertThat(savedMember.getIsDark()).isEqualTo(isDark.toString());
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() {
        try {
            //given
            Member newMember = commonTestCode.saveNewMember();
            //when
            newMember.deleteMember();
            //then
            Assertions.assertThat(newMember.getIsDelete()).isEqualTo(BooleanYN.Y.toString());
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("로그인한 회원의 모든 정보 조회")
    void getAllMemberInfo() {
        try {
            //given
            //when
            MemberInfoDTO savedMember = memberService.getAllMemberInfo(CommonTestCode.MEMBER_ID);
            //then
            Assertions.assertThat(savedMember.getEmail()).isEqualTo("abc@def.com");
        } catch (NotExistMemberException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    @DisplayName("로그인한 회원의 닉네임만 조회")
    void getNicknameInfo() {
        try {
            //given
            //when
            MemberNameDTO savedMember = memberService.getNicknameInfo(CommonTestCode.MEMBER_ID);
            //then
            Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
        } catch (NotExistMemberException e) {
            log.error(e.getMessage());
        }
        //then
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
        try {
            //given
            //when
            Member savedMember = memberService.getMemberById(CommonTestCode.MEMBER_ID);
            //then
            Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
        } catch (NotExistMemberException e) {
            log.error(e.getMessage());
        }
    }

}