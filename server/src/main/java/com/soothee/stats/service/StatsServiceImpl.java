package com.soothee.stats.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
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
    public MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, MonthParam monthParam) {
        MonthlyStatsDTO result = dairyRepository.findDiaryStatsInMonth(memberId, monthParam).orElse(new MonthlyStatsDTO());
        Double condCnt = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        ).getCount().doubleValue();
        ConditionRatio mostCond = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 1, condCnt).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        ).get(0);
        result.setMostCondId(mostCond.getCondId());
        result.setMostCondRatio(mostCond.getCondRatio());
        return result;
    }

    @Override
    public MonthlyContentsDTO getMonthlyContents(Long memberId, String type, MonthParam monthParam) {
        return MonthlyContentsDTO.builder()
                .count(dairyRepository.findDiaryContentCntInMonth(memberId, type, monthParam).orElse(0))
                .highest(dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, "high").orElse(new DateContents()))
                .lowest(dairyRepository.findDiaryContentInMonthHL(memberId, type, monthParam, "low").orElse(new DateContents()))
                .build();
    }

    @Override
    public MonthlyConditionsDTO getMonthlyConditionList(Long memberId, MonthParam monthParam) {
        dairyRepository.findDiaryStatsInMonth(memberId, monthParam).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        MonthlyConditionsDTO result = dairyConditionRepository.getAllDairyConditionCntInMonth(memberId, monthParam).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        List<ConditionRatio> condRatioList = dairyConditionRepository.findConditionRatioListInMonth(memberId, monthParam, 3, result.getCount().doubleValue()).orElseThrow(
                () -> new MyException(HttpStatus.NO_CONTENT, MyErrorMsg.NO_CONTENTS)
        );
        result.setCondiList(condRatioList);
        return result;
    }

    @Override
    public WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, WeekParam weekParam) {
        WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(memberId, weekParam).orElse(new WeeklyStatsDTO());
        if (result.getCount() > 2) {
            result.setScoreList(dairyRepository.findDiaryScoresInWeekly(memberId, weekParam).orElse(new ArrayList<>()));
        }
        return result;
    }
}
