package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.oauth2.domain.AuthenticatedUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name(NAME)
                .email(EMAIL)
                .oauth2ClientId(OAUTH2_CLIENT_ID)
                .snsType(SNS_TYPE).build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("OAuth2 로그인 시, 받은 정보로 회원 조회")
    void getMemberForOAuth2() {

    }

    @Test
    @DisplayName("회원 가입")
    void saveMember() {
        //given
        Member newMember = Member.builder()
                .email("def@def.com")
                .name("새사용자")
                .oauth2ClientId("22222")
                .snsType(SnsType.GOOGLE).build();
        //when
        memberService.saveMember(newMember);
        //then
        Optional<Member> optional = memberRepository.findByEmail("def@def.com");
        Member mem1 = optional.orElseThrow(NullPointerException::new);
        Assertions.assertThat("22222").isEqualTo(mem1.getOauth2ClientId());
    }

    @Test
    @DisplayName("회원 닉네임 수정")
    void updateName() {
        //given
        String newName = "새 이름";
        Optional<Member> optional1 = memberRepository.findByEmail(EMAIL);
        Member mem1 = optional1.orElseThrow(NullPointerException::new);
        //when
        mem1.updateName(newName);
        //then
        Optional<Member> optional2 = memberRepository.findByEmail(EMAIL);
        Member mem2 = optional2.orElseThrow(NullPointerException::new);
        Assertions.assertThat(mem2.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("회원 다크모드 수정")
    void updateDarkMode() {
        //given
        String isDarkY = "Y";
        Optional<Member> optional1 = memberRepository.findByEmail(EMAIL);
        Member mem1 = optional1.orElseThrow(NullPointerException::new);
        //when
        mem1.updateDarkModeYN(isDarkY);
        //then
        Optional<Member> optional2 = memberRepository.findByEmail(EMAIL);
        Member mem2 = optional2.orElseThrow(NullPointerException::new);
        Assertions.assertThat(mem2.getIsDark()).isEqualTo(isDarkY);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() {
        //given
        Optional<Member> optional1 = memberRepository.findByEmail(EMAIL);
        Member mem1 = optional1.orElseThrow(NullPointerException::new);
        //when
        mem1.deleteMember();
        //then
        Optional<Member> optional2 = memberRepository.findByEmail(EMAIL);
        Member mem2 = optional2.orElseThrow(NullPointerException::new);
        Assertions.assertThat(mem2.getIsDelete()).isEqualTo("Y");
    }

    @Test
    @DisplayName("로그인한 회원의 모든 정보 조회")
    void getAllMemberInfo() {
    }

    @Test
    @DisplayName("로그인한 회원의 닉네임만 조회")
    void getNicknameInfo() {
    }

    @AfterEach
    void tearDown() {
        memberRepository.delete(member);
    }
}