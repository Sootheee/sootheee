package com.soothee;

import com.soothee.member.domain.Member;
import com.soothee.util.CommonTestCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@EnableJpaAuditing
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class TestDBTest {
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void 테스트디비에서_회원조회_성공하기() {
        //given
        Member member = commonTestCode.getSavedMember();
        //when
        String nickname = member.getName();
        //then
        Assertions.assertThat(nickname).isEqualTo("사용자0");
    }
}
