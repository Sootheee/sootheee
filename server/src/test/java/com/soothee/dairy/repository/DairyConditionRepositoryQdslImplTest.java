package com.soothee.dairy.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.service.ConditionService;
import com.soothee.reference.service.WeatherService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@Transactional
class DairyConditionRepositoryQdslImplTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DairyRepository dairyRepository;
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

    @BeforeEach
    void setUp() {
        member = Member.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .oauth2ClientId(OAUTH2_CLIENT_ID)
                        .snsType(SNS_TYPE).build();
        memberRepository.save(member);
        Dairy newDairy1 = Dairy.builder()
                                .member(member)
                                .date(LocalDate.of(2024,10,10))
                                .score(2.0)
                                .weather(weatherService.getWeatherById(1L))
                                .build();
        dairyRepository.save(newDairy1);
        DairyCondition d1dc1 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(7L))
                                            .dairy(newDairy1)
                                            .orderNo(0)
                                            .build();
        dairyConditionRepository.save(d1dc1);
        DairyCondition d1dc2 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(2L))
                                            .dairy(newDairy1)
                                            .orderNo(1)
                                            .build();
        dairyConditionRepository.save(d1dc2);
        DairyCondition d1dc3 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(6L))
                                            .dairy(newDairy1)
                                            .orderNo(2)
                                            .build();
        dairyConditionRepository.save(d1dc3);
        DairyCondition d1dc4 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(10L))
                                            .dairy(newDairy1)
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
                                            .condition(conditionService.getConditionById(11L))
                                            .dairy(newDairy2)
                                            .orderNo(0)
                                            .build();
        dairyConditionRepository.save(d2dc1);
        DairyCondition d2dc2 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(3L))
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
                                            .condition(conditionService.getConditionById(5L))
                                            .dairy(newDairy2)
                                            .orderNo(3)
                                            .build();
        dairyConditionRepository.save(d2dc4);
        Dairy newDairy3 = Dairy.builder()
                                .member(member)
                                .date(LocalDate.of(2024,10,12))
                                .score(1.0)
                                .weather(weatherService.getWeatherById(2L))
                                .build();
        dairyRepository.save(newDairy3);
        DairyCondition d3dc1 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(4L))
                                            .dairy(newDairy3)
                                            .orderNo(0)
                                            .build();
        dairyConditionRepository.save(d3dc1);
        DairyCondition d3dc2 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(1L))
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
                                            .condition(conditionService.getConditionById(7L))
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
    void findMostOneCondIdInMonth() {
        //given
        //when
        Long result = dairyConditionRepository.findMostOneCondIdInMonth(member.getMemberId(), 2024, 10).orElseThrow();
        //then
        Assertions.assertThat(result).isEqualTo(5L);
    }

    @AfterEach
    void tearDown() {
        dairyConditionRepository.deleteAll();
        dairyRepository.deleteAll();
        memberRepository.deleteAll();
    }
}