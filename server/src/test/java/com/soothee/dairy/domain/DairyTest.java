package com.soothee.dairy.domain;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.repository.ConditionRepository;
import com.soothee.reference.repository.WeatherRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class DairyTest {
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
    private Weather weather;
    @Autowired
    private ConditionRepository conditionRepository;

    @BeforeEach
    void setUp() {
        weather = weatherRepository.findByWeatherId(1L).orElseThrow();

        member = Member.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .oauth2ClientId(OAUTH2_CLIENT_ID)
                        .snsType(SNS_TYPE).build();
        memberRepository.save(member);

        dairy = Dairy.builder()
                    .member(member)
                    .date(LocalDate.of(2024,10,10))
                    .score(2.0)
                    .weather(weather)
                    .build();
        dairyRepository.save(dairy);
    }

    @Test
    void updateDairy() {
        //given
        List<Dairy> savedDairyList = dairyRepository.findByMemberMemberIdAndIsDelete(member.getMemberId(), "N").orElseThrow();
        Dairy savedDairy = savedDairyList.get(0);
        Dairy newDairy = Dairy.builder()
                                .member(member)
                                .date(LocalDate.of(2024,10,10))
                                .score(2.0)
                                .weather(weatherRepository.findByWeatherId(1L).orElseThrow())
                                .thank("thanks")
                                .build();
        //when
        savedDairy.updateDairy(newDairy);
        //then
        Assertions.assertThat(dairy.getThank()).isEqualTo("thanks");
    }

    @Test
    void deleteDairy() {
        //given
        List<Dairy> savedDairyList = dairyRepository.findByMemberMemberIdAndIsDelete(member.getMemberId(), "N").orElseThrow();
        Dairy savedDairy = savedDairyList.get(0);
        //when
        savedDairy.deleteDairy();
        //then
        Assertions.assertThat(savedDairy.getIsDelete()).isEqualTo("Y");
    }

    @AfterEach
    void tearDown() {
        dairyRepository.delete(dairy);
        memberRepository.delete(member);
    }
}