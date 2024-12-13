package com.soothee.stats.service;

import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.dairy.service.DairyConditionService;
import com.soothee.dairy.service.DairyService;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
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
    private final MemberService memberService;
    private final DairyRepository dairyRepository;
    private final DairyConditionRepository dairyConditionRepository;

    @Override
    public MonthlyStatsDTO getMonthlyStatsInfo(AuthenticatedUser loginInfo, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        MonthlyStatsDTO result = dairyRepository.findDiaryStatsInMonth(memberId, year, month).orElse(new MonthlyStatsDTO());
        Long mostCondId = dairyConditionRepository.findMostOneCondIdInMonth(memberId, year, month).orElse(0L);
        result.setMostCondId(mostCondId);
        return result;
    }

    @Override
    public MonthlyContentsDTO getMonthlyContents(AuthenticatedUser loginInfo, String type, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        return MonthlyContentsDTO.builder()
                .count(dairyRepository.findDiaryContentCntInMonth(memberId, type, year, month).orElse(0))
                .highest(dairyRepository.findDiaryContentInMonth(memberId, type, year, month, "high").orElse(new DateContents()))
                .lowest(dairyRepository.findDiaryContentInMonth(memberId, type, year, month, "low").orElse(new DateContents()))
                .build();
    }

    @Override
    public WeeklyStatsDTO getWeeklyStatsInfo(AuthenticatedUser loginInfo, Integer year, Integer week) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        WeeklyStatsDTO result = dairyRepository.findDiaryStatsInWeekly(memberId, year, week).orElse(new WeeklyStatsDTO());
        if (result.getDairyCnt() > 2) {
            result.setScoreList(dairyRepository.findDiaryScoresInWeekly(memberId, year, week).orElse(new ArrayList<>()));
        }
        return result;
    }
}
