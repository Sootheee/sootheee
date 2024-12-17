package com.soothee.stats.service;

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
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void getMonthlyStatsInfo() {
        //given
        //when
        MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH);
        //then
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        Assertions.assertThat(result.getMostCondId()).isEqualTo(1L);
    }

    @Test
    void getMonthlyContentsT() {
        //given
        //when
        MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, "thanks", CommonTestCode.YEAR, CommonTestCode.MONTH);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(2);
        Assertions.assertThat(result.getHighest().getContent()).isEqualTo("땡큐");
        Assertions.assertThat(result.getLowest().getContent()).isEqualTo("감사");
    }

    @Test
    void getMonthlyContentsL() {
        //given
        //when
        MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, "learn", CommonTestCode.YEAR, CommonTestCode.MONTH);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(2);
        Assertions.assertThat(result.getHighest().getContent()).isEqualTo("배운");
        Assertions.assertThat(result.getLowest().getContent()).isEqualTo("공부");
    }

    @Test
    void getWeeklyStatsInfo() {
        //given
        //when
        WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK);
        //then
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        Assertions.assertThat(result.getDairyCnt()).isEqualTo(5);
        Double resultScore = 0.0;
        for (DateScore score : result.getScoreList()) {
            if (Objects.equals(score.getDate(), CommonTestCode.DATE1)) {
                resultScore = score.getScore();
            }
        }
        Assertions.assertThat(resultScore).isEqualTo(2.0);

    }
}