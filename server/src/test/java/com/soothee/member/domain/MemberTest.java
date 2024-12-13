package com.soothee.member.domain;

import com.soothee.common.constants.SnsType;
import com.soothee.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@AutoConfigureDataJpa
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class MemberTest {
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
    @DisplayName("회원 닉네임 수정 성공")
    void updateName() {
        //given
        String newName = "수정한이름";
        Member mem1 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        //when
        mem1.updateName(newName);
        //then
        Member mem2 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        Assertions.assertThat(newName).isEqualTo(mem2.getName());
    }

    @Test
    @DisplayName("회원 다크모드 수정 성공")
    void updateDarkModeYN() {
        //given
        String isDarkY = "Y";
        Member mem1 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        //when
        mem1.updateDarkModeYN(isDarkY);
        //then
        Member mem2 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        Assertions.assertThat(isDarkY).isEqualTo(mem2.getIsDark());
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteMember() {
        //given
        Member mem1 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        //when
        mem1.deleteMember();

        //then
        Member mem2 = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();
        Assertions.assertThat("Y").isEqualTo(mem2.getIsDelete());
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }
}