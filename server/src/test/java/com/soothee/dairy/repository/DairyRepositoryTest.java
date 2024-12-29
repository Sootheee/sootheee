package com.soothee.dairy.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.config.TestConfig;
import com.soothee.dairy.controller.response.DairyAllResponse;
import com.soothee.dairy.controller.response.DairyScoresResponse;
import com.soothee.stats.controller.response.DateContents;
import com.soothee.stats.controller.response.MonthlyDairyStats;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.controller.response.DateScore;
import com.soothee.stats.controller.response.WeeklyDairyStats;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class DairyRepositoryTest {
    @Autowired
    private DairyRepository dairyRepository;

    @Test
    void findByMemberMemberIdAndIsDeleteOrderByDairyId() {
        //given
        //when
        List<Dairy> list = dairyRepository.findByMemberMemberIdAndIsDeleteOrderByDairyId(CommonTestCode.MEMBER_ID, BooleanYN.N).orElseThrow(NullPointerException::new);
        //then
        for (Dairy dairy : list) {
            if (Objects.equals(dairy.getDairyId(), CommonTestCode.DAIRY_ID1)) {
                Assertions.assertThat(dairy.getScore()).isEqualTo(2.0);
            }
        }
    }

    @Test
    void findByDairyIdAndIsDelete() {
        //given
        //when
        Dairy searchDairy = dairyRepository.findDairyByDairyId(CommonTestCode.DAIRY_ID1).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(searchDairy.getScore()).isEqualTo(2.0);
    }


    @Test
    void findByMemberIdYearMonth() {
        //given
        //when
        List<DairyScoresResponse> list = dairyRepository.findScoreListInMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH).orElseThrow(NullPointerException::new);
        //then
        for (DairyScoresResponse scoresDTO : list) {
            if (Objects.equals(scoresDTO.getDairyId(), CommonTestCode.DAIRY_ID1)) {
                Assertions.assertThat(scoresDTO.getScore()).isEqualTo(2.0);
            }
        }
    }

    @Test
    void findByDate() {
        //given
        //when
        DairyAllResponse result = dairyRepository.findAllDairyInfoByDate(CommonTestCode.MEMBER_ID, CommonTestCode.DATE1).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findByMemberDiaryId() {
        //given
        //when
        DairyAllResponse result = dairyRepository.findAllDairyInfoByDiaryId(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID1).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findDiaryStatsInMonth() {
        //given
        //when
        MonthlyDairyStats result = dairyRepository.findDairyStatsInMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(5);
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
    }

    @Test
    void findDiaryContentCntInMonth() {
        //given
        //when
        Integer result = dairyRepository.getMonthlyContentsCount(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result).isEqualTo(3);
    }

    @Test
    void findDiaryContentInMonth() {
        //given
        //when
        DateContents result = dairyRepository.findOneContentByHighestOrLowestScoreInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.HIGH).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getDairyId()).isEqualTo(CommonTestCode.DAIRY_ID2);
        Assertions.assertThat(result.getDate()).isEqualTo(CommonTestCode.DATE2);
        Assertions.assertThat(result.getContent()).isEqualTo("땡큐");
    }

    @Test
    void findDiaryStatsInWeekly() {
        //given
        //when
        WeeklyDairyStats result = dairyRepository.findDairyStatsInWeek(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(5);
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
    }

    @Test
    void findDiaryScoresInWeekly() {
        //given
        //when
        List<DateScore> result = dairyRepository.findDiaryScoreInWeek(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getScore()).isEqualTo(2.0);
    }

    @Test
    void findDiaryContentInMonthSortThanksDate() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.DATE).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("oh");
    }

    @Test
    void findDiaryContentInMonthSortThanksHigh() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.HIGH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("땡큐");
    }

    @Test
    void findDiaryContentInMonthSortThanksLow() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.LOW).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("감사");
    }

    @Test
    void findDiaryContentInMonthSortLearnDate() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.DATE).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("no");
    }

    @Test
    void findDiaryContentInMonthSortLearnHigh() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.HIGH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("배운");
    }

    @Test
    void findDiaryContentInMonthSortLearnLow() {
        //given
        //when
        List<DateContents> result = dairyRepository.findSortedContentDetailInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.LOW).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("공부");
    }
}