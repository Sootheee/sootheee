package com.soothee.stats.service;

import com.soothee.stats.dto.MonthlyConditionsDTO;
import com.soothee.stats.dto.MonthlyContentsDTO;
import com.soothee.stats.dto.MonthlyStatsDTO;
import com.soothee.stats.dto.WeeklyStatsDTO;

public interface StatsService {
    /**
     * 로그인한 계정이 지정한 년도/달의 요약 정보 조회</hr>
     * 1. 한 달간 작성한 일기 개수</br>
     * 2. 한 달동안 오늘의 점수 평균값</br>
     * 3. 한 달간 가장 많이 선택한 1개의 컨디션의 일련번호</br>
     * [컨디션 선택 횟수가 같은 경우]</br>
     * (1) 먼저 선택한 순</br>
     * (2) 긍정 > 보통 > 부정 카테고리 순</br>
     * (3) 카테고리별 먼저 등록된 순</br>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 현재 로그인한 계정의 일련번호
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return MonthlyStatsDTO : 월간 요약 정보
     */
    MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, Integer year, Integer month);

    /**
     * 로그인한 계정이 지정한 년도/달의 고마운/배운 일 통계 조회</hr>
     * 1. 한 달간 작성한 고마운/배운 일 횟수</br>
     * 2. 한 달간 작성한 고마운/배운 일 중 가장 높은 점수 날의 고마운/배운 일</br>
     * 3. 한 달간 작성한 고마운/배운 일 중 가장 낮은 점수 날의 고마운/배운 일
     *
     * @param memberId Long : 현재 로그인한 계정의 일련번호
     * @param type      String : 고마운/배운 일 타입
     * @param year      Integer : 지정한 년도
     * @param month     Integer : 지정한 달
     * @return MonthlyContentsDTO : 월간 고마운/배운 일 정보
     */
    MonthlyContentsDTO getMonthlyContents(Long memberId, String type, Integer year, Integer month);

    /**
     * 월간 선택 횟수 상위 최대 3개 컨디션 리스트 조회</hr>
     *
     * @param memberId  Long : 현재 로그인한 계정의 일련번호
     * @param year      Integer : 지정한 년도
     * @param month      Integer : 지정한 월
     * @return MonthlyConditionDTO : 월간 선택 횟수 상위 최대 3개 컨디션 리스트
     */
    MonthlyConditionsDTO getMonthlyConditionList(Long memberId, Integer year, Integer month);

    /**
     * 로그인한 계정이 지정한 년도/주의 요약 정보 조회</hr>
     * 1. 한 주간 작성한 일기 개수</br>
     * 2. 한 주동안 오늘의 점수 평균값</br>
     * 3. 한 주동안 작성한 일기의 날짜와 오늘의 점수
     *
     * @param memberId  Long : 현재 로그인한 계정의 일련번호
     * @param year      Integer : 지정한 년도
     * @param week      Integer : 지정한 주
     * @return WeeklyStatsDTO : 주간 요약 정보
     */
    WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, Integer year, Integer week);
}
