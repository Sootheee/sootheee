package com.soothee;

import com.soothee.common.constants.SnsType;
import com.soothee.member.entity.MemberEntity;
import com.soothee.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
public class TestDBTests {
    @Autowired
    private MemberRepository memberRepository;

    private String memberName = "사용자0";
    private String email = "abc@def.com";
    private String isDelete = "N";
    private SnsType snsType = SnsType.KAKAOTALK;

    @Test
    void 테스트디비에서_회원조회_성공하기() {
        //given
        MemberEntity member = memberRepository.findByMemberName(memberName);
        //when
        String email = member.getEmail();
        //then
        Assertions.assertThat(email).isEqualTo("abc@def.com");
    }

    @BeforeEach
    void insertDefaultMember() {
        MemberEntity member = MemberEntity.builder()
                .memberName(memberName)
                .email(email)
                .isDelete(isDelete)
                .snsType(snsType).build();
        memberRepository.save(member);
    }

    @AfterEach
    void deleteDefaultMember() {
        memberRepository.deleteAll();
    }
}
