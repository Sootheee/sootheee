package com.soothee.dairy.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.MonthlyDairyScoreDTO;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.repository.WeatherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class DairyRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DairyRepository dairyRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;
    private Dairy dairy;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name(NAME)
                .email(EMAIL)
                .oauth2ClientId(OAUTH2_CLIENT_ID)
                .snsType(SNS_TYPE).build();
        memberRepository.save(member);

        dairy = Dairy.builder()
                .member(memberRepository.findByEmail(EMAIL).orElseThrow())
                .date(LocalDate.of(2024,10,10))
                .score(2.0)
                .weather(weatherRepository.findByWeatherId(1L).orElseThrow())
                .build();
        dairyRepository.save(dairy);
    }

    @Test
    void findByMemberIdAndDate() {
        //given
        Member writer = memberRepository.findByEmail(EMAIL).orElseThrow();
        //when
        Optional<List<MonthlyDairyScoreDTO>> optional = dairyRepository.findByMemberIdAndDate(writer.getMemberId(), 2024, 10);
        List<MonthlyDairyScoreDTO> resultList = optional.orElseThrow();
        MonthlyDairyScoreDTO result = resultList.get(0);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findByMemberMemberId() {
        //given
        Member writer = memberRepository.findByEmail(EMAIL).orElseThrow();
        //when
        Optional<List<Dairy>> optional = dairyRepository.findByMemberMemberId(writer.getMemberId());
        List<Dairy> resultList = optional.orElseThrow();
        Dairy result = resultList.get(0);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @AfterEach
    void tearDown() {
        dairyRepository.delete(dairy);
        memberRepository.delete(member);
    }
}