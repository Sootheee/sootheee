package com.soothee.member.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MemberRepositoryTest {
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
    @DisplayName("email로 member 조회")
    void findByEmail() {
        //given
        //when
        Optional<Member> optional = memberRepository.findByEmail(EMAIL);
        Member mem1 = optional.orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(mem1.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("oauth2ClientId와 SnsType으로 member 조회")
    void findByOauth2ClientIdAndSnsType() {
        //given
        //when
        Optional<Member> optional = memberRepository.findByOauth2ClientIdAndSnsType(OAUTH2_CLIENT_ID, SNS_TYPE);
        Member mem1 = optional.orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(mem1.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("oauth2ClientId로 member 조회")
    void findByOauth2ClientId() {
        //given
        //when
        Optional<Member> optional = memberRepository.findByOauth2ClientId(OAUTH2_CLIENT_ID);
        Member mem1 = optional.orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(mem1.getName()).isEqualTo(NAME);
    }

    @AfterEach
    void tearDown() {
        memberRepository.delete(member);
    }
}