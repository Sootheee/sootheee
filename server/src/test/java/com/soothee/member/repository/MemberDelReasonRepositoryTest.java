package com.soothee.member.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.domain.Member;
import com.soothee.util.CommonTestCode;
import com.soothee.member.domain.MemberDelReason;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class MemberDelReasonRepositoryTest {
    @Autowired
    private MemberDelReasonRepository memberDelReasonRepository;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void findByMemberMemberId() {
        try {
            //given
            Member savedNewMember = commonTestCode.deleteMember();
            //when
            List<MemberDelReason> result = memberDelReasonRepository.findByMemberMemberId(savedNewMember.getMemberId())
                    .orElseThrow(NullPointerException::new);
            //then
            Assertions.assertThat(result.size()).isEqualTo(3);
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }
}