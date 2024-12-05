package com.soothee.stats.service;

import com.soothee.dairy.service.DairyConditionService;
import com.soothee.dairy.service.DairyService;
import com.soothee.member.domain.Member;
import com.soothee.member.service.MemberService;
import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.stats.dto.MonthlyStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final MemberService memberService;
    private final DairyService dairyService;
    private final DairyConditionService dairyConditionService;

    @Override
    public MonthlyStatsDTO getMonthlyStatsInfo(AuthenticatedUser loginInfo, Integer year, Integer month) {
        Member loginMember = memberService.getLoginMember(loginInfo);
        MonthlyStatsDTO result = dairyService.getDairyStatsInMonth(loginMember.getMemberId(), year, month);
        Long mostCondId = dairyConditionService.getMostOneCondIdInMonth(loginMember.getMemberId(), year, month);
        result.setMostCondId(mostCondId);
        return result;
    }
        Member loginMember = memberService.getLoginMember(loginInfo);
        return dairyService.getDairyCntAvgInMonth(loginMember.getMemberId(), year, month);
    }
}
