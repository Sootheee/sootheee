package com.soothee.stats.service;

import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.stats.dto.MonthlyStatsDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface StatsService {
    /**
     * 로그인한 계정이 지정한 년도/달의 요약 정보 조회</hr>
     * 1. 한 달간 작성한 일기 개수
     * 2. 한 달동안 오늘의 점수 평균값
     * 3. 한 달간 가장 많이 선택한 1개의 컨디션의 일련번호
     * [컨디션 선택 횟수가 같은 경우]
     * (1) 먼저 선택한 순
     * (2) 긍정 > 보통 > 부정 카테고리 순
     * (3) 카테고리별 먼저 등록된 순
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return MonthlyStatsDTO : 월간 요약 정보
     */
    MonthlyStatsDTO getMonthlyStatsInfo(AuthenticatedUser loginInfo, Integer year, Integer month);
}
