package com.soothee.dairy.service;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.service.ConditionService;
import com.soothee.reference.service.WeatherService;
import com.soothee.stats.dto.MonthlyStatsDTO;
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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class DairyServiceImplTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DairyRepository dairyRepository;
    @Autowired
    private DairyService dairyService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private DairyConditionRepository dairyConditionRepository;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;
    private Dairy dairy1;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name(NAME)
                .email(EMAIL)
                .oauth2ClientId(OAUTH2_CLIENT_ID)
                .snsType(SNS_TYPE).build();
        memberRepository.save(member);

        dairy1 = Dairy.builder()
                .member(member)
                .date(LocalDate.of(2024,10,10))
                .score(2.0)
                .weather(weatherService.getWeatherById(1L))
                .build();
        dairyRepository.save(dairy1);
        DairyCondition d1dc1 = DairyCondition.builder()
                .condition(conditionService.getConditionById(1L))
                .dairy(dairy1)
                .orderNo(0)
                .build();
        dairyConditionRepository.save(d1dc1);
        DairyCondition d1dc2 = DairyCondition.builder()
                .condition(conditionService.getConditionById(3L))
                .dairy(dairy1)
                .orderNo(1)
                .build();
        dairyConditionRepository.save(d1dc2);
        DairyCondition d1dc3 = DairyCondition.builder()
                .condition(conditionService.getConditionById(5L))
                .dairy(dairy1)
                .orderNo(2)
                .build();
        dairyConditionRepository.save(d1dc3);
        DairyCondition d1dc4 = DairyCondition.builder()
                .condition(conditionService.getConditionById(10L))
                .dairy(dairy1)
                .orderNo(3)
                .build();
        dairyConditionRepository.save(d1dc4);
        Dairy newDairy2 = Dairy.builder()
                .member(member)
                .date(LocalDate.of(2024,10,11))
                .score(5.5)
                .weather(weatherService.getWeatherById(4L))
                .build();
        dairyRepository.save(newDairy2);
        DairyCondition d2dc1 = DairyCondition.builder()
                .condition(conditionService.getConditionById(4L))
                .dairy(newDairy2)
                .orderNo(0)
                .build();
        dairyConditionRepository.save(d2dc1);
        DairyCondition d2dc2 = DairyCondition.builder()
                .condition(conditionService.getConditionById(1L))
                .dairy(newDairy2)
                .orderNo(1)
                .build();
        dairyConditionRepository.save(d2dc2);
        DairyCondition d2dc3 = DairyCondition.builder()
                .condition(conditionService.getConditionById(12L))
                .dairy(newDairy2)
                .orderNo(2)
                .build();
        dairyConditionRepository.save(d2dc3);
        DairyCondition d2dc4 = DairyCondition.builder()
                .condition(conditionService.getConditionById(2L))
                .dairy(newDairy2)
                .orderNo(3)
                .build();
        dairyConditionRepository.save(d2dc4);
        Dairy newDairy3 = Dairy.builder()
                .member(member)
                .date(LocalDate.of(2024,10,12))
                .score(1.0)
                .weather(weatherService.getWeatherById(5L))
                .build();
        dairyRepository.save(newDairy3);
        DairyCondition d3dc1 = DairyCondition.builder()
                .condition(conditionService.getConditionById(1L))
                .dairy(newDairy3)
                .orderNo(0)
                .build();
        dairyConditionRepository.save(d3dc1);
        DairyCondition d3dc2 = DairyCondition.builder()
                .condition(conditionService.getConditionById(10L))
                .dairy(newDairy3)
                .orderNo(1)
                .build();
        dairyConditionRepository.save(d3dc2);
        DairyCondition d3dc3 = DairyCondition.builder()
                .condition(conditionService.getConditionById(5L))
                .dairy(newDairy3)
                .orderNo(2)
                .build();
        dairyConditionRepository.save(d3dc3);
        DairyCondition d3dc4 = DairyCondition.builder()
                .condition(conditionService.getConditionById(3L))
                .dairy(newDairy3)
                .orderNo(3)
                .build();
        dairyConditionRepository.save(d3dc4);
        Dairy newDairy4 = Dairy.builder()
                .member(member)
                .date(LocalDate.of(2024,10,13))
                .score(9.5)
                .weather(weatherService.getWeatherById(3L))
                .build();
        dairyRepository.save(newDairy4);
        DairyCondition d4dc1 = DairyCondition.builder()
                .condition(conditionService.getConditionById(12L))
                .dairy(newDairy4)
                .orderNo(0)
                .build();
        dairyConditionRepository.save(d4dc1);
        DairyCondition d4dc2 = DairyCondition.builder()
                .condition(conditionService.getConditionById(5L))
                .dairy(newDairy4)
                .orderNo(1)
                .build();
        dairyConditionRepository.save(d4dc2);
        DairyCondition d4dc3 = DairyCondition.builder()
                .condition(conditionService.getConditionById(8L))
                .dairy(newDairy4)
                .orderNo(2)
                .build();
        dairyConditionRepository.save(d4dc3);
        DairyCondition d4dc4 = DairyCondition.builder()
                .condition(conditionService.getConditionById(9L))
                .dairy(newDairy4)
                .orderNo(3)
                .build();
        dairyConditionRepository.save(d4dc4);
    }

    @Test
    void getAllDairyMonthly() {
    }

    @Test
    void getDairyByDate() {
    }

    @Test
    void getDairyByDairyId() {
    }

    @Test
    void registerDairy() {
    }

    @Test
    void modifyDairy() {
    }

    @Test
    void deleteDairy() {
    }

    @Test
    void getDairyStatsInMonth() {
        //given
        //when
        MonthlyStatsDTO result = dairyService.getDairyStatsInMonth(member.getMemberId(), 2024, 10);
        //then
        Assertions.assertThat(result.getDairyCnt()).isEqualTo(4);
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
    }

    @AfterEach
    void tearDown() {
        dairyRepository.deleteAll();
        memberRepository.deleteAll();
    }
}