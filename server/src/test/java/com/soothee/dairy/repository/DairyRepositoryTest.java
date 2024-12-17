package com.soothee.dairy.repository;

import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.stats.dto.DateScore;
import com.soothee.stats.dto.WeeklyStatsDTO;
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
@Import(TestConfig.class)
class DairyRepositoryTest {
    @Autowired
    private DairyRepository dairyRepository;

    @Test
    void findByMemberMemberIdAndIsDeleteOrderByDairyId() {
        //given
        //when
        List<Dairy> list = dairyRepository.findByMemberMemberIdAndIsDeleteOrderByDairyId(CommonTestCode.MEMBER_ID, "N")
                                        .orElseThrow(NullPointerException::new);
        //then
        Dairy result = Dairy.builder().build();
        for (Dairy dairy : list) {
            if (Objects.equals(dairy.getDairyId(), CommonTestCode.DAIRY_ID1)) {
                result = dairy;
            }
        }
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findByDairyId() {
        //given
        //when
        Dairy searchDairy = dairyRepository.findByDairyId(CommonTestCode.DAIRY_ID1).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(searchDairy.getScore()).isEqualTo(2.0);
    }


    @Test
    void findByMemberIdYearMonth() {
        //given
        //when
        List<DairyScoresDTO> list = dairyRepository.findByMemberIdYearMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH)
                                                .orElseThrow(NullPointerException::new);
        //then
        DairyScoresDTO result = new DairyScoresDTO();
        for (DairyScoresDTO scoresDTO : list) {
            if (Objects.equals(scoresDTO.getDairyId(), CommonTestCode.DAIRY_ID1)) {
                result = scoresDTO;
            }
        }
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findByDate() {
        //given
        //when
        DairyDTO result = dairyRepository.findByDate(CommonTestCode.MEMBER_ID, CommonTestCode.DATE1)
                                            .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findByDiaryId() {
        //given
        //when
        DairyDTO result = dairyRepository.findByMemberDiaryId(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID1)
                                            .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(2.0);
    }

    @Test
    void findDiaryStatsInWeekly() {
        //given
        //when
        WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK)
                                                .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.getDairyCnt()).isEqualTo(5);
        Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
    }

    @Test
    void findDiaryScoresInWeekly() {
        //given
        //when
        List<DateScore> result = dairyRepository.findDiaryScoresInWeekly(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK)
                                                    .orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.get(0).getScore()).isEqualTo(2.0);
    }
}