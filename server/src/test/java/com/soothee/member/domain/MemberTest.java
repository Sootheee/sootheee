package com.soothee.member.domain;

import com.soothee.common.constants.BooleanYN;
import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Slf4j
@Import(TestConfig.class)
class MemberTest {
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    @DisplayName("회원 닉네임 수정 성공")
    void updateName() {
        //given
        String newName = "수정할이름";
        Member savedMember = commonTestCode.getSavedMember();
        //when
        savedMember.updateName(newName);
        //then
        Assertions.assertThat(savedMember.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("회원 다크모드 수정 성공")
    void updateDarkModeYN() {
        //given
        BooleanYN isDark = BooleanYN.Y;
        Member savedMember = commonTestCode.getSavedMember();
        //when
        savedMember.updateDarkModeYN(isDark);
        //then
        Assertions.assertThat(savedMember.getIsDark()).isEqualTo(BooleanYN.Y);
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteMember() {
        //given
        Member savedMember = commonTestCode.getSavedMember();
        //when
        savedMember.deleteMember();
        //then
        Assertions.assertThat(savedMember.getIsDelete()).isEqualTo(BooleanYN.Y);
    }
}