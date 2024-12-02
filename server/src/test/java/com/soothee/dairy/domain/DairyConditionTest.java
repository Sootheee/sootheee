package com.soothee.dairy.domain;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.repository.DairyConditionRepository;
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
import java.util.List;
import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class DairyConditionTest {
    @Autowired
    private DairyConditionRepository dairyConditionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DairyRepository dairyRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private ConditionRepository conditionRepository;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;
    private Dairy dairy;
    private DairyCondition dairyCondition;

    @BeforeEach
    void setUp() {
        Weather weather = weatherRepository.findByWeatherId(1L).orElseThrow();

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
        Condition condition = conditionRepository.getReferenceById(1L);
        dairyCondition = DairyCondition.builder()
                                        .dairy(dairy)
                                        .condition(condition)
                                        .build();
        dairyConditionRepository.save(dairyCondition);
    }

    @Test
    void deleteDairyCondition() {
        //given
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        Optional<List<DairyCondition>> optionalDairyCondition = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N");
        List<DairyCondition> dairyConditionList = optionalDairyCondition.orElseThrow();
        DairyCondition savedDairyCondition = dairyConditionList.get(0);
        //when
        savedDairyCondition.deleteDairyCondition();
        //then
        Assertions.assertThat(savedDairyCondition.getIsDelete()).isEqualTo("Y");
    }

    @AfterEach
    void tearDown() {
        dairyConditionRepository.delete(dairyCondition);
        dairyRepository.delete(dairy);
        memberRepository.delete(member);
    }
}