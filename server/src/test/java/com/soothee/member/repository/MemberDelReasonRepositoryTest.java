package com.soothee.member.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.repository.DelReasonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MemberDelReasonRepositoryTest {
    @Autowired
    private MemberDelReasonRepository memberDelReasonRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DelReasonRepository delReasonRepository;
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
        DelReason dr = delReasonRepository.findByReasonId(1L).orElseThrow();
        MemberDelReason mdr = MemberDelReason.builder()
                                            .member(member)
                                            .delReason(dr)
                                            .build();
        memberDelReasonRepository.save(mdr);
    }

    @Test
    void findByMemberMemberId() {
        //given
        //when
        List<MemberDelReason> result = memberDelReasonRepository.findByMemberMemberId(member.getMemberId()).orElseThrow();
        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        memberDelReasonRepository.deleteAll();
        memberRepository.deleteAll();
    }
}