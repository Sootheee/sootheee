package com.soothee.stats.service;

import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.stats.dto.DateContents;
import com.soothee.stats.dto.MonthlyContentsDTO;
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.stats.dto.WeeklyStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final DairyRepository dairyRepository;
    private final DairyConditionRepository dairyConditionRepository;

    @Override
    public MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, Integer year, Integer month) {
        MonthlyStatsDTO result = dairyRepository.findDiaryStatsInMonth(memberId, year, month).orElse(new MonthlyStatsDTO());
        Long mostCondId = dairyConditionRepository.findMostOneCondIdInMonth(memberId, year, month).orElse(0L);
        result.setMostCondId(mostCondId);
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
    public WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, Integer year, Integer week) {
        WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(memberId, year, week).orElse(new WeeklyStatsDTO());
        if (result.getDairyCnt() > 2) {
            result.setScoreList(dairyRepository.findDiaryScoresInWeekly(memberId, year, week).orElse(new ArrayList<>()));
        }
        return result;
    }
}
