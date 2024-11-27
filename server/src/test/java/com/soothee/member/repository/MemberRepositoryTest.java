package com.soothee.member.repository;

import com.soothee.common.constants.Role;
import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                            .email("abc@abc.com")
                            .name("사용자")
                            .snsType(SnsType.KAKAOTALK)
                            .oauth2ClientId("111")
                            .build();
        memberRepository.save(member);
    }

    @Test
    void findByEmail() {
        //given - setUp()
        //when
        Optional<Member> optional = memberRepository.findByEmail("abc@abc.com");
        Member findMember = optional.orElseGet(() -> Member.builder().build());
        String expectedMemberName = findMember.getName();
        //then
        Assertions.assertThat(expectedMemberName).isEqualTo("사용자");

    }

    @Test
    void findByOauth2ClientIdAndSnsType() {
        //given - setUp()
        //when
        Optional<Member> optional = memberRepository.findByOauth2ClientIdAndSnsType("111", SnsType.KAKAOTALK);
        Member findMember = optional.orElseGet(() -> Member.builder().build());
        String expectedMemberName = findMember.getName();
        //then
        Assertions.assertThat(expectedMemberName).isEqualTo("사용자");
    }

    @AfterEach
    void tearDown() {

    }
}