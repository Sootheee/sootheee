package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.config.TestConfig;
import com.soothee.stats.dto.*;
import com.soothee.util.CommonTestCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Import(TestConfig.class)
class StatsServiceTest {
    @Autowired
    private StatsService statsService;

    @Test
    void getMonthlyStatsInfo() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(CommonTestCode.MEMBER_ID, monthParam);
        //then
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        Assertions.assertThat(result.getMostCondId()).isEqualTo(1L);
    }

    @Test
    void getMonthlyContentsT() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, ContentType.THANKS.toString(), monthParam);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getHighest().getContent()).isEqualTo("땡큐");
        Assertions.assertThat(result.getLowest().getContent()).isEqualTo("감사");
    }

    @Test
    void getMonthlyContentsL() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, ContentType.LEARN.toString(), monthParam);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getHighest().getContent()).isEqualTo("배운");
        Assertions.assertThat(result.getLowest().getContent()).isEqualTo("공부");
    }

    @Test
    void getWeeklyStatsInfo() {
        //given
        WeekParam weekParam = new WeekParam(CommonTestCode.YEAR, CommonTestCode.WEEK);
        //when
        WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(CommonTestCode.MEMBER_ID, weekParam);
        //then
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        Assertions.assertThat(result.getCount()).isEqualTo(5);
        Double resultScore = 0.0;
        for (DateScore score : result.getScoreList()) {
            if (Objects.equals(score.getDate(), CommonTestCode.DATE1)) {
                resultScore = score.getScore();
            }
        }
        Assertions.assertThat(resultScore).isEqualTo(2.0);
    }

    @Test
    void getMonthlyConditionList() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyConditionsDTO result = statsService.getMonthlyConditionList(CommonTestCode.MEMBER_ID, monthParam);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(20);
        Assertions.assertThat(result.getCondiList().get(0).getCondId()).isEqualTo(1L);
        Assertions.assertThat(result.getCondiList().get(0).getCondRatio()).isEqualTo(25.0);
        Assertions.assertThat(result.getCondiList().get(1).getCondId()).isEqualTo(2L);
        Assertions.assertThat(result.getCondiList().get(1).getCondRatio()).isEqualTo(25.0);
        Assertions.assertThat(result.getCondiList().get(2).getCondId()).isEqualTo(7L);
        Assertions.assertThat(result.getCondiList().get(2).getCondRatio()).isEqualTo(25.0);
    }

    @Test
    void getAllContentsInMonthThanksDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS.toString(), monthParam, SortType.DATE.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("oh");

    }

    @Test
    void getAllContentsInMonthThanksHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS.toString(), monthParam, SortType.HIGH.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("땡큐");
    }

    @Test
    void getAllContentsInMonthThanksLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS.toString(), monthParam, SortType.LOW.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("감사");
    }

    @Test
    void getAllContentsInMonthLearnDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN.toString(), monthParam, SortType.DATE.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("no");
    }

    @Test
    void getAllContentsInMonthLearnHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN.toString(), monthParam, SortType.HIGH.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("배운");
    }
    @Test
    void getAllContentsInMonthLearnLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN.toString(), monthParam, SortType.LOW.toString());
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(3);
        Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("공부");
    }
}