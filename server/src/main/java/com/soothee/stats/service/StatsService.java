package com.soothee.stats.service;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.stats.dto.*;

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
     * @param memberId      Long : 현재 로그인한 계정의 일련번호
     * @param monthParam    MonthParam : 지정한 년도/달
     * @return MonthlyStatsDTO : 월간 요약 정보
     */
    MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, MonthParam monthParam);

    /**
     * 로그인한 계정이 지정한 년도/달의 감사한/배운 일 통계 조회</hr>
     * 1. 한 달간 작성한 감사한/배운 일 횟수</br>
     * 2. 한 달간 작성한 감사한/배운 일 중 가장 높은 점수 날의 감사한/배운 일</br>
     * 2-1. 가장 높은 점수 날의 일기 일련번호</br>
     * 2-2. 가장 높은 점수 날짜</br>
     * 2-3. 가장 높은 점수</br>
     * 2-4. 가장 높은 점수 날의 감사한/배운 일 내용</br>
     * 3. 한 달간 작성한 감사한/배운 일 중 가장 낮은 점수 날의 감사한/배운 일</br>
     * 3-1. 가장 낮은 점수 날의 일기 일련번호</br>
     * 3-2. 가장 낮은 점수 날짜</br>
     * 3-3. 가장 낮은 점수</br>
     * 3-4. 가장 낮은 점수 날의 감사한/배운 일 내용
     *
     * @param memberId      Long : 현재 로그인한 계정의 일련번호
     * @param type          String : 감사한/배운 일 타입
     * @param monthParam    MonthParam : 지정한 년도/달
     * @return MonthlyContentsDTO : 월간 감사한/배운 일 정보
     */
    MonthlyContentsDTO getMonthlyContents(Long memberId, String type, MonthParam monthParam);

    /**
     * 로그인한 계정이 지정한 년도/주차의 요약 정보 조회</hr>
     * 1. 한 주간 작성한 일기 개수</br>
     * 2. 한 주동안 오늘의 점수 평균값</br>
     * 3. 한 주동안 작성한 일기의 날짜와 오늘의 점수
     *
     * @param memberId  Long : 현재 로그인한 계정의 일련번호
     * @param weekParam   WeekParam : 지정한 년도/주차
     * @return WeeklyStatsDTO : 주간 요약 정보
     */
    WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, WeekParam weekParam);

    /**
     * 월간 선택 횟수 상위 최대 3개 컨디션 리스트 조회</hr>
     * 1. 한 달간 기록한 컨디션 개수</br>
     * 2. 한 달동안 기록한 컨디션 중 상위 최대 3개의 컨디션 정보</br>
     * 2-1. 컨디션의 일련번호</br>
     * 2-2. 전체 선택한 컨디션 중 해당 컨디션의 비율
     *
     * @param memberId     Long : 현재 로그인한 계정의 일련번호
     * @param monthParam   MonthParam : 지정한 년도/달
     * @return MonthlyConditionDTO : 월간 선택 횟수 상위 최대 3개 컨디션 리스트
     */
    MonthlyConditionsDTO getMonthlyConditionList(Long memberId, MonthParam monthParam);

    /**
     * 로그인한 계정이 지정한 년도/달에 작성한 모든 감사한/배운 일 리스트 조회</hr>
     * 1. 한 달간 작성한 감사한/배운 일 횟수</br>
     * 2. 한 달간 작성한 감사한/배운 일 리스트</br>
     * 2-1. 일기 일련번호</br>
     * 2-2. 일기 날짜</br>
     * 2-3. 일기 오늘의 점수</br>
     * 2-4. 해당 감사한/배운 일 내용
     *
     * @param memberId      Long : 현재 로그인한 계정의 일련번호
     * @param type          String : 감사한/배운 일 타입
     * @param monthParam    MonthParam : 지정한 년도/달
     * @param orderBy       String : 조회 순서
     * @return MonthlyAllContentsDTO : 월간 작성한 모든 감사한/배운 일의 개수와 정보 리스트
     */
    MonthlyAllContentsDTO getAllContentsInMonth(Long memberId, String type, MonthParam monthParam, String orderBy);
}
