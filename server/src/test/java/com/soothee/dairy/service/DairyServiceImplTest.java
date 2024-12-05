package com.soothee.dairy.service;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.repository.ConditionRepository;
import com.soothee.reference.service.ConditionService;
import com.soothee.reference.service.WeatherService;
import com.soothee.stats.dto.MonthlyAvgDTO;
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

    @AfterEach
    void tearDown() {
        dairyRepository.deleteAll();
        memberRepository.deleteAll();
    }
}