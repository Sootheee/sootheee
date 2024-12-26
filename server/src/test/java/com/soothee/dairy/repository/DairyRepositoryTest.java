package com.soothee.dairy.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.config.TestConfig;
import com.soothee.stats.dto.DateContents;
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.stats.dto.DateScore;
import com.soothee.stats.dto.WeeklyStatsDTO;
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
        List<Dairy> list = dairyRepository.findByMemberMemberIdAndIsDeleteOrderByDairyId(CommonTestCode.MEMBER_ID, BooleanYN.N.toString()).orElseThrow(NullPointerException::new);
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
        Dairy searchDairy = dairyRepository.findByDairyIdAndIsDelete(CommonTestCode.DAIRY_ID1, BooleanYN.N.toString()).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(searchDairy.getScore()).isEqualTo(2.0);
    }


    @Test
    void findByMemberIdYearMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DairyScoresDTO> list = dairyRepository.findByMemberIdYearMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow(NullPointerException::new);
        //then
        for (DairyScoresDTO scoresDTO : list) {
            if (Objects.equals(scoresDTO.getDairyId(), CommonTestCode.DAIRY_ID1)) {
                Assertions.assertThat(scoresDTO.getScore()).isEqualTo(2.0);
            }
        }
    }

    @Test
    void findByDate() {
        //given
        try {
            //when
            DairyDTO result = dairyRepository.findByDate(CommonTestCode.MEMBER_ID, CommonTestCode.DATE1).orElseThrow(NullPointerException::new).get(0);
            //then
            Assertions.assertThat(result.getScore()).isEqualTo(2.0);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findByMemberDiaryId() {
        //given
        try {
            //when
            DairyDTO result = dairyRepository.findByMemberDiaryId(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID1).orElseThrow(NullPointerException::new).get(0);
            //then
            Assertions.assertThat(result.getScore()).isEqualTo(2.0);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findDiaryStatsInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyStatsDTO result = dairyRepository.findDiaryStatsInMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow(NullPointerException::new).get(0);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(5);
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findDiaryContentCntInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        int result = dairyRepository.findDiaryContentCntInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result).isEqualTo(3);
    }

    @Test
    void findDiaryContentInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            DateContents result = dairyRepository.findDiaryContentInMonthHL(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam, SortType.HIGH).orElseThrow(NullPointerException::new).get(0);
            //then
            Assertions.assertThat(result.getDairyId()).isEqualTo(CommonTestCode.DAIRY_ID2);
            Assertions.assertThat(result.getDate()).isEqualTo(CommonTestCode.DATE2);
            Assertions.assertThat(result.getContent()).isEqualTo("땡큐");
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findDiaryStatsInWeekly() {
        //given
        WeekParam weekParam = new WeekParam(CommonTestCode.YEAR, CommonTestCode.WEEK);
        try {
            //when
            WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(CommonTestCode.MEMBER_ID, weekParam).orElseThrow(NullPointerException::new).get(0);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(5);
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findDiaryScoresInWeekly() {
        //given
        WeekParam weekParam = new WeekParam(CommonTestCode.YEAR, CommonTestCode.WEEK);
        //when
        List<DateScore> result = dairyRepository.findDiaryScoresInWeekly(CommonTestCode.MEMBER_ID, weekParam).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getScore()).isEqualTo(2.0);
    }

    @Test
    void findDiaryContentInMonthSortThanksDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.THANKS,monthParam, SortType.DATE).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("oh");
    }
    @Test
    void findDiaryContentInMonthSortThanksHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.THANKS,monthParam, SortType.HIGH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("땡큐");
    }
    @Test
    void findDiaryContentInMonthSortThanksLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.THANKS,monthParam, SortType.LOW).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("감사");
    }
    @Test
    void findDiaryContentInMonthSortLearnDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.LEARN,monthParam, SortType.DATE).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("no");
    }
    @Test
    void findDiaryContentInMonthSortLearnHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.LEARN,monthParam, SortType.HIGH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("배운");
    }
    @Test
    void findDiaryContentInMonthSortLearnLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        List<DateContents> result = dairyRepository.findDiaryContentInMonthSort(CommonTestCode.MEMBER_ID, ContentType.LEARN, monthParam, SortType.LOW).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getContent()).isEqualTo("공부");
    }
}