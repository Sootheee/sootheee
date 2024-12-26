package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.custom.exception.*;
import com.soothee.stats.dto.*;

public interface StatsService {
    /**
     * 로그인한 계정이 지정한 년도/달의 요약 정보 조회
     * [1] 한 달 동안 작성한 일기 갯수
     * [2] 한 달 동안 오늘의 점수 평균값
     * [3] 한 달 동안 가장 많이 선택한 1개의 컨디션의 일련번호
     * [4] 한 달 동안 가장 많이 선택한 1개의 컨디션의 비율
     * [컨디션 선택 횟수가 같은 경우]
     * (1) 먼저 선택한 순
     * (2) 긍정 > 보통 > 부정 카테고리 순
     * (3) 카테고리별 먼저 등록된 순
     * - 삭제된 일기 제외
     * - 삭제된 일기-컨디션 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 통계 결과가 1개 초과로 중복된 경우 Exception 발생
     * 3. 선택한 컨디션이 있지만 통계 결과를 불러오지 못한 경우 Exception 발생
     * 4. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param monthParam 지정한 년도/달
     * @return 한 달 동안 요약 정보
     */
    MonthlyStatsDTO getMonthlyStatsInfo(Long memberId, MonthParam monthParam) throws DuplicatedResultException, IncorrectValueException, NullValueException, ErrorToSearchStatsException, NotFoundDetailInfoException;

    /**
     * 로그인한 계정이 지정한 년도/달의 감사한/배운 일 통계 조회
     * [1] 한 달 동안 작성한 감사한/배운 일 횟수
     * [2] 한 달 동안 작성한 감사한/배운 일 중 가장 높은 점수 날의 감사한/배운 일
     * [2-1] 가장 높은 점수 날의 일기 일련번호
     * [2-2] 가장 높은 점수 날짜
     * [2-3] 가장 높은 점수
     * [2-4] 가장 높은 점수 날의 감사한/배운 일 내용
     * [3] 한 달 동안 작성한 감사한/배운 일 중 가장 낮은 점수 날의 감사한/배운 일
     * [3-1] 가장 낮은 점수 날의 일기 일련번호
     * [3-2] 가장 낮은 점수 날짜
     * [3-3] 가장 낮은 점수
     * [3-4] 가장 낮은 점수 날의 감사한/배운 일 내용
     * - 삭제된 일기 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 감사한/배운 일을 기록한 날 중 가장 높은/낮은 점수를 기록한 날의 감사한/배운 일 정보가 없는 경우 Exception 발생
     * 3. 통계 결과가 1개 초과로 중복된 경우 Exception 발생
     * 4. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param type 감사한/배운 일 타입
     * @param monthParam 지정한 년도/달
     * @return 한 달 동안 감사한/배운 일 정보
     */
    MonthlyContentsDTO getMonthlyContents(Long memberId, ContentType type, MonthParam monthParam) throws NotFoundDetailInfoException, DuplicatedResultException, IncorrectValueException, NullValueException, ErrorToSearchStatsException;

    /**
     * 로그인한 계정이 지정한 년도/주차의 요약 정보 조회
     * [1] 한 주 동안 작성한 일기 갯수
     * [2] 한 주 동안 오늘의 점수 평균값
     * [3] 한 주 동안 작성한 일기의 날짜와 오늘의 점수
     * - 삭제된 일기 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 통계 결과가 1개 초과로 중복된 경우 Exception 발생
     * 3. 한 주간 작성한 일기가 있지만, 일기 세부 정보를 가지고 올 수 없는 경우
     * 4. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     * 5. 조회한 모든 컨디션의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param weekParam 지정한 년도/주차
     * @return 한 주 동안 요약 정보
     */
    WeeklyStatsDTO getWeeklyStatsInfo(Long memberId, WeekParam weekParam) throws DuplicatedResultException, ErrorToSearchStatsException, NotFoundDetailInfoException, IncorrectValueException, NullValueException;

    /**
     * 한 달 동안 선택 횟수 상위 최대 3개 컨디션 리스트 조회
     * [1] 한 달 동안 기록한 컨디션 갯수
     * [2] 한 달 동안 기록한 컨디션 중 상위 최대 3개의 컨디션 정보
     * [2-1] 컨디션의 일련번호
     * [2-2] 전체 선택한 컨디션 중 해당 컨디션의 비율
     * - 삭제된 일기 제외
     * - 삭제된 일기-컨디션 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     * 3. 선택한 컨디션이 있지만 통계 결과를 불러오지 못한 경우 Exception 발생
     * 4. 조회한 모든 컨디션의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param monthParam 지정한 년도/달
     * @return 한 달 동안 선택 횟수 상위 최대 3개 컨디션 리스트
     */
    MonthlyConditionsDTO getMonthlyConditionList(Long memberId, MonthParam monthParam) throws ErrorToSearchStatsException, IncorrectValueException, NullValueException, NotFoundDetailInfoException;

    /**
     * 로그인한 계정이 지정한 년도/달에 작성한 모든 감사한/배운 일 리스트 조회
     * [1] 한 달 동안 작성한 감사한/배운 일 횟수
     * [2] 한 달 동안 작성한 감사한/배운 일 리스트
     * [2-1] 일기 일련번호
     * [2-2] 일기 날짜
     * [2-3] 일기 오늘의 점수
     * [2-4] 해당 감사한/배운 일 내용
     * - 삭제된 일기 제외
     * 1. 조회 결과가 아예 나오지 않는 경우 (0이라도 나와야함) Exception 발생
     * 2. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     * 3. 작성한 감사한/배운 일이 있지만 통계 결과를 불러오지 못한 경우 Exception
     * 4. 조회한 감사한/배운 일의 갯수와 세부 정보 리스트의 길이가 일치하지 않는 경우 Exception 발생
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param type 감사한/배운 일 타입
     * @param monthParam 지정한 년도/달
     * @param orderBy 조회 순서
     * @return 한 달 동안 작성한 모든 감사한/배운 일의 갯수와 정보 리스트
     */
    MonthlyAllContentsDTO getAllContentsInMonth(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy) throws ErrorToSearchStatsException, NotFoundDetailInfoException, IncorrectValueException, NullValueException;
}
