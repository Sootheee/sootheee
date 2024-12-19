package com.soothee.member.service;

import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Import(TestConfig.class)
class MemberDelReasonServiceTest {
    @Autowired
    private MemberDelReasonService memberDelReasonService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void saveDeleteReasons() {
        //given
        List<Long> reasonIds = commonTestCode.getDelReasonIds();
        Member savedMember = commonTestCode.getSavedMember();
        //when
        memberDelReasonService.saveDeleteReasons(savedMember, reasonIds);
        //then
        List<MemberDelReason> savedMDR = commonTestCode.getSavedMemberDelReasons();
        Assertions.assertThat(savedMDR.size()).isEqualTo(3);
    }

    @Test
    void getMemberDelReasonByMemberId() {
        //given
        Member newMember = commonTestCode.deleteMember();
        //when
        List<MemberDelReason> savedMDR = memberDelReasonService.getMemberDelReasonByMemberId(newMember.getMemberId());
        //then
        Assertions.assertThat(savedMDR.size()).isEqualTo(3);
    }
}