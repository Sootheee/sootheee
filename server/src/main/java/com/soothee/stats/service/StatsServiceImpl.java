package com.soothee.stats.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.stats.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final DairyRepository dairyRepository;
    private final DairyConditionRepository dairyConditionRepository;

    @Override
    public MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, Integer year, Integer month) {
        MonthlyStatsDTO result = dairyRepository.findDiaryStatsInMonth(memberId, year, month).orElse(new MonthlyStatsDTO());
        Double condCnt = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, year, month).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        ).getCount().doubleValue();
        ConditionRatio mostCond = dairyConditionRepository.findConditionRatioListInMonth(memberId, year, month, 1, condCnt).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        ).get(0);
        result.setMostCondId(mostCond.getCondId());
        result.setMostCondRatio(mostCond.getCondRatio());
        return result;
    }

    @Override
    public MonthlyContentsDTO getMonthlyContents(Long memberId, String type, Integer year, Integer month) {
        return MonthlyContentsDTO.builder()
                .count(dairyRepository.findDiaryContentCntInMonth(memberId, type, year, month).orElse(0))
                .highest(dairyRepository.findDiaryContentInMonth(memberId, type, year, month, "high").orElse(new DateContents()))
                .lowest(dairyRepository.findDiaryContentInMonth(memberId, type, year, month, "low").orElse(new DateContents()))
                .build();
    }

    @Override
    public MonthlyConditionsDTO getMonthlyConditionList(Long memberId, Integer year, Integer month) {
        dairyRepository.findDiaryStatsInMonth(memberId, year, month).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        MonthlyConditionsDTO result = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, year, month).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        List<ConditionRatio> condRatioList = dairyConditionRepository.findConditionRatioListInMonth(memberId, year, month, 3, result.getCount().doubleValue()).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        result.setCondiList(condRatioList);
        return result;
    }

    @Override
    public WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, Integer year, Integer week) {
        WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(memberId, year, week).orElse(new WeeklyStatsDTO());
        if (result.getCount() > 2) {
            result.setScoreList(dairyRepository.findDiaryScoresInWeekly(memberId, year, week).orElse(new ArrayList<>()));
        }
        return result;
    }
}
