package com.soothee.dairy.service;

import com.soothee.common.constants.SnsType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.service.ConditionService;
import com.soothee.reference.service.WeatherService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class DairyConditionServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DairyConditionServiceImplTest.class);
    @Autowired
    private DairyConditionServiceImpl dairyConditionService;
    @Autowired
    private DairyConditionRepository dairyConditionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DairyRepository dairyRepository;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private ConditionService conditionService;
    private final String NAME = "사용자0";
    private final String EMAIL = "abc@def.com";
    private final SnsType SNS_TYPE = SnsType.KAKAOTALK;
    private final String OAUTH2_CLIENT_ID = "111111";
    private Member member;
    private Dairy dairy;
    private DairyCondition dairyCondition;
    private List<Long> condIds;

    @BeforeEach
    void setUp() {
        Weather weather = weatherService.getWeatherById(1L);
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
                        .weather(weather)
                        .build();
        dairyRepository.save(dairy);
    }

    @Test
    void getConditionsIdListByDairy() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        dairyConditionService.saveConditions(condIds, dairy);
        //when
        List<Long> dairyConditionList = dairyConditionService.getConditionsIdListByDairy(dairy.getDairyId());
        //then
        Assertions.assertThat(dairyConditionList.get(0)).isEqualTo(1L);
        Assertions.assertThat(dairyConditionList.get(1)).isEqualTo(2L);
        Assertions.assertThat(dairyConditionList.get(2)).isEqualTo(3L);
    }

    @Test
    void saveConditions() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        //when
        dairyConditionService.saveConditions(condIds, dairy);
        //then
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        List<DairyCondition> dairyConditionList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        Assertions.assertThat(dairyConditionList.size()).isEqualTo(3);
    }

    @Test
    void updateConditions() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        dairyConditionService.saveConditions(condIds, dairy);
        //when
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        List<DairyCondition> beforeDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < beforeDcList.size(); i++) {
            LOGGER.info("before {} : {}, {}", i+1, beforeDcList.get(i).getDairyCondId(),beforeDcList.get(i).getOrderNo());
        }
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        dairyConditionService.updateConditions(savedDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < afterDcList.size(); i++) {
            LOGGER.info("after {} : {}, {}", i+1, afterDcList.get(i).getDairyCondId(),afterDcList.get(i).getOrderNo());
        }
        Assertions.assertThat(afterDcList.size()).isEqualTo(3);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
    }

    @Test
    void updateConditionsBiggerInput() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        dairyConditionService.saveConditions(condIds, dairy);
        //when
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        List<DairyCondition> beforeDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < beforeDcList.size(); i++) {
            LOGGER.info("before {} : {}, {}", i+1, beforeDcList.get(i).getDairyCondId(),beforeDcList.get(i).getOrderNo());
        }
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        newCondIds.add(5L);
        newCondIds.add(6L);
        dairyConditionService.updateConditions(savedDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < afterDcList.size(); i++) {
            LOGGER.info("after {} : {}, {}", i+1, afterDcList.get(i).getDairyCondId(),afterDcList.get(i).getOrderNo());
        }
        Assertions.assertThat(afterDcList.size()).isEqualTo(5);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
        Assertions.assertThat(afterDcList.get(3).getCondition().getCondId()).isEqualTo(5L);
        Assertions.assertThat(afterDcList.get(4).getCondition().getCondId()).isEqualTo(6L);
    }

    @Test
    void updateConditionsBiggerCur() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        condIds.add(4L);
        condIds.add(5L);
        dairyConditionService.saveConditions(condIds, dairy);
        //when
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        List<DairyCondition> beforeDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < beforeDcList.size(); i++) {
            LOGGER.info("before {} : {}, {}", i+1, beforeDcList.get(i).getDairyCondId(),beforeDcList.get(i).getOrderNo());
        }
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        dairyConditionService.updateConditions(savedDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        for (int i = 0; i < afterDcList.size(); i++) {
            LOGGER.info("after {} : {}, {}", i+1, afterDcList.get(i).getDairyCondId(),afterDcList.get(i).getOrderNo());
        }
        Assertions.assertThat(afterDcList.size()).isEqualTo(3);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
    }

    @Test
    void deleteDairyConditionsOfDairy() {
        //given
        condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        dairyConditionService.saveConditions(condIds, dairy);
        Member savedmember = memberRepository.findByEmail(EMAIL).orElseThrow();
        Dairy savedDairy = dairyRepository.findByMemberMemberIdAndIsDelete(savedmember.getMemberId(), "N").orElseThrow().get(0);
        List<DairyCondition> savedDairyConditionList = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow();
        //when
        dairyConditionService.deleteDairyConditionsOfDairy(savedDairy);
        //then
        Assertions.assertThat(dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(savedDairy.getDairyId(), "N").orElseThrow().size()).isEqualTo(0);
    }

    @Test
    void getMostOneCondIdInMonth() {
        //given
        DairyCondition d1dc1 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(1L))
                                            .dairy(dairy)
                                            .orderNo(0)
                                            .build();
        dairyConditionRepository.save(d1dc1);
        DairyCondition d1dc2 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(3L))
                                            .dairy(dairy)
                                            .orderNo(1)
                                            .build();
        dairyConditionRepository.save(d1dc2);
        DairyCondition d1dc3 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(7L))
                                            .dairy(dairy)
                                            .orderNo(2)
                                            .build();
        dairyConditionRepository.save(d1dc3);
        DairyCondition d1dc4 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(13L))
                                            .dairy(dairy)
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
                                            .condition(conditionService.getConditionById(7L))
                                            .dairy(newDairy2)
                                            .orderNo(2)
                                            .build();
        dairyConditionRepository.save(d2dc3);
        DairyCondition d2dc4 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(3L))
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
                                            .condition(conditionService.getConditionById(11L))
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
                                            .condition(conditionService.getConditionById(7L))
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
                                            .condition(conditionService.getConditionById(1L))
                                            .dairy(newDairy4)
                                            .orderNo(0)
                                            .build();
        dairyConditionRepository.save(d4dc1);
        DairyCondition d4dc2 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(3L))
                                            .dairy(newDairy4)
                                            .orderNo(1)
                                            .build();
        dairyConditionRepository.save(d4dc2);
        DairyCondition d4dc3 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(4L))
                                            .dairy(newDairy4)
                                            .orderNo(2)
                                            .build();
        dairyConditionRepository.save(d4dc3);
        DairyCondition d4dc4 = DairyCondition.builder()
                                            .condition(conditionService.getConditionById(7L))
                                            .dairy(newDairy4)
                                            .orderNo(3)
                                            .build();
        dairyConditionRepository.save(d4dc4);
        //when
        Long condId = dairyConditionService.getMostOneCondIdInMonth(member.getMemberId(), 2024, 10);
        //then
        Assertions.assertThat(condId).isEqualTo(1L);
    }

    @AfterEach
    void tearDown() {
        dairyConditionRepository.deleteAll();
        dairyRepository.deleteAll();
        memberRepository.deleteAll();
    }
}