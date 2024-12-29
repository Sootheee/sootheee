package com.soothee.member.service;

import com.soothee.config.TestConfig;
import com.soothee.custom.exception.NullValueException;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Import(TestConfig.class)
class MemberDelReasonServiceTest {
    @Autowired
    private MemberDelReasonService memberDelReasonService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void saveDeleteReasons() {
        //given
        List<String> reasonIds = commonTestCode.getDelReasonIds();
        Member savedMember = commonTestCode.getSavedMember();
        try {
            //when
            memberDelReasonService.saveDeleteReasons(savedMember, reasonIds);
            //then
            List<MemberDelReason> savedMDR = commonTestCode.getSavedMemberDelReasons();
            Assertions.assertThat(savedMDR.size()).isEqualTo(3);
        } catch (NullValueException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMemberDelReasonByMemberId() {
        try {
            //given
            Member newMember = commonTestCode.deleteMember();
            //when
            List<MemberDelReason> savedMDR = memberDelReasonService.getMemberDelReasonByMemberId(newMember.getMemberId());
            //then
            Assertions.assertThat(savedMDR.size()).isEqualTo(3);
        } catch (NullValueException e) {
            log.error("\n", e);
        }
    }
}