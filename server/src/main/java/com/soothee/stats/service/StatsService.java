package com.soothee.stats.service;

import com.soothee.oauth2.domain.AuthenticatedUser;
import com.soothee.stats.dto.MonthlyAvgDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface StatsService {
    /**
     * 로그인한 계정이 지정한 년도/달의 요약 정보 조회</hr>
     * 1. 한달간 작성한 일기 개수
     * 2. 한달동안 오늘의 점수 평균값
     * 3. 한달간 가장 많이 선택한 1개의 컨디션의 일련번호
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인한 계정 정보
     * @param year      Integer : 지정한 년도
     * @param month      Integer : 지정한 달
     * @return MonthlyAvgDTO : 월간 요약 정보
     */
    MonthlyAvgDTO getMonthlyAvgInfo(AuthenticatedUser loginInfo, Integer year, Integer month);
}
