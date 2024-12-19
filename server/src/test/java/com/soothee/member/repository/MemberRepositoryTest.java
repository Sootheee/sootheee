package com.soothee.member.repository;

import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@Import(TestConfig.class)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("oauth2ClientId와 SnsType으로 member 조회")
    void findByOauth2ClientIdAndIsDeleteAndSnsTypeAndIsDelete() {
        //given
        //when
        Member savedMember = memberRepository.findByOauth2ClientIdAndSnsTypeAndIsDelete(CommonTestCode.OAUTH2, CommonTestCode.SNS_TYPE, "N")
                .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

    @Test
    @DisplayName("oauth2ClientId로 member 조회")
    void findByOauth2ClientIdAndIsDelete() {
        //given
        //when
        Member savedMember = memberRepository.findByOauth2ClientIdAndIsDelete(CommonTestCode.OAUTH2, "N")
                .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

    @Test
    @DisplayName("email로 member 조회")
    void findByEmail() {
        //given
        //when
        Member savedMember = memberRepository.findByEmail(CommonTestCode.EMAIL).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo("사용자0");
    }

    @Test
    void findByMemberId() {
        //given
        //when
        Member result = memberRepository.findByMemberId(CommonTestCode.MEMBER_ID).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.getName()).isEqualTo("사용자0");
    }
}
