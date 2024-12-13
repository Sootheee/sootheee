package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.member.repository.MemberDelReasonRepository;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.service.DelReasonService;
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

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class MemberDelReasonServiceTest {
    @Autowired
    private MemberDelReasonService memberDelReasonService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDelReasonRepository delReasonRepository;

    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;
    private List<Long> reasonIdList;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name(NAME)
                .email(EMAIL)
                .oauth2ClientId(OAUTH2_CLIENT_ID)
                .snsType(SNS_TYPE).build();
        memberRepository.save(member);
        reasonIdList = new ArrayList<>();
        reasonIdList.add(1L);
        reasonIdList.add(2L);
        reasonIdList.add(3L);
    }

    @Test
    void saveDeleteReasons() {
        //given
        //when
        Member savedMember = memberRepository.findByEmail(EMAIL).orElseThrow();
        memberDelReasonService.saveDeleteReasons(savedMember, reasonIdList);
        //then
        List<MemberDelReason> savedMDR = memberDelReasonService.getMemberDelReasonByMemberId(member.getMemberId());
        Assertions.assertThat(savedMDR.size()).isEqualTo(3);
    }

    @Test
    void getMemberDelReasonByMemberId() {
        //given
        memberDelReasonService.saveDeleteReasons(member, reasonIdList);
        //when
        List<MemberDelReason> savedMDR = memberDelReasonService.getMemberDelReasonByMemberId(member.getMemberId());
        Assertions.assertThat(savedMDR.size()).isEqualTo(3);
    }

    @AfterEach
    void tearDown() {
        delReasonRepository.deleteAll();
        memberRepository.deleteAll();
    }
}