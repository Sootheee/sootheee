package com.soothee;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@EnableJpaAuditing
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class TestDBTest {
    @Autowired
    private MemberRepository memberRepository;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;

    @Test
    void 테스트디비에서_회원조회_성공하기() {
        //given
        Optional<Member> optional = memberRepository.findByEmail(EMAIL);
        Member member = optional.get();
        //when
        String nickname = member.getName();
        //then
        Assertions.assertThat(nickname).isEqualTo(NAME);
    }

    @BeforeEach
    void insertDefaultMember() {
        member = Member.builder()
                .name(NAME)
                .email(EMAIL)
                .oauth2ClientId(OAUTH2_CLIENT_ID)
                .snsType(SNS_TYPE).build();
        memberRepository.save(member);
    }

    @AfterEach
    void deleteDefaultMember() {
        memberRepository.delete(member);
    }
}
