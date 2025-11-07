package com.soothee.dairy.repository;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.dairy.controller.response.DairyAllResponse;
import com.soothee.dairy.controller.response.DairyScoresResponse;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.controller.response.DateContents;
import com.soothee.stats.controller.response.DateScore;
import com.soothee.stats.controller.response.MonthlyDairyStats;
import com.soothee.stats.controller.response.WeeklyDairyStats;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DairyRepositoryQdsl {
    /**
     * 로그인한 계정이 조회할 년도-월에 작성한 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param month 조회할 월
     * @return 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    Optional<List<DairyScoresResponse>> findScoreListInMonth(Long memberId, Integer year, Integer month);

    /**
     * 로그인한 계정이 조회할 날에 작성한 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param date     조회할 날짜
     * @return 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyAllResponse>> findAllDairyInfoByDate(Long memberId, LocalDate date);

    /**
     * 로그인한 계정이 작성한 해당 일련번호의 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param dairyId  조회할 일기 일련번호
     * @return 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyAllResponse>> findAllDairyInfoByDiaryId(Long memberId, Long dairyId);

    /**
     * 일기 일련번호로 해당 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param dairyId 일기 일련번호
     * @return 조회된 일기 정보 (null 가능)
     */
    Optional<List<Dairy>> findDairyByDairyId(Long dairyId);

    /**
     * 로그인한 계정이 조회할 년도/달에 작성한 일기 갯수 & 오늘의 점수 평균 조회
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param month 조회할 월
     * @return 조회할 년도/달 동안 작성한 일기 갯수와 오늘의 점수 평균 (null 가능)
     */
    Optional<List<MonthlyDairyStats>> findDairyStatsInMonth(Long memberId, Integer year, Integer month);

    /**
     * 해당 달에 작성한 감사한/배운 일 횟수
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param type       감사한/배운 일 중 타입
     * @param year 조회할 년도
     * @param month 조회할 월
     * @return 작성한 감사한/배운 일 횟수 (null 가능)
     */
    Optional<Integer> getMonthlyContentsCount(Long memberId, ContentType type, Integer year, Integer month);

    /**
     * 해당 달에 작성한 감사한/배운 일 중 가장 높은/낮은 점수를 기록한 날의 감사한/배운 일
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param type       감사한/배운 일 중 타입
     * @param year 조회할 년도
     * @param month 조회할 월
     * @param sort       가장 높은/낮은 점수 중 타입
     * @return 기록한 날짜, 가장 높은/낮은 점수, 감사한/배운 일 내용 정보 리스트 (null 가능)
     */
    Optional<List<DateContents>> findOneContentByHighestOrLowestScoreInMonth(Long memberId, ContentType type, Integer year, Integer month, SortType sort);

    /**
     * 로그인한 계정이 조회할 년도/주차에 작성한 일기 갯수 & 오늘의 점수 평균 조회
     * - 삭제한 일기 제외
     *
     * @param memberId  로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param week 조회할 주차
     * @return 조회할 년도/주차 동안 작성한 일기 갯수와 오늘의 점수 평균 (null 가능)
     */
    Optional<List<WeeklyDairyStats>> findDairyStatsInWeek(Long memberId, Integer year, Integer week);

    /**
     * 로그인한 계정이 조회할 년도/주차에 작성한 일기 날짜와 점수 리스트
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param year 조회할 년도
     * @param week 조회할 주차
     * @return 조회할 년도/주차 동안 작성한 일기 날짜와 점수 리스트 (null 가능)
     */
    Optional<List<DateScore>> findDiaryScoreInWeek(Long memberId, Integer year, Integer week);

    /**
     * 해당 달에 작성한 모든 감사한/배운 일 정보 리스트 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param type 감사한/배운 일 중 타입
     * @param year 조회할 년도
     * @param month 조회할 월
     * @param orderBy 조회 순서 타입
     * @return 기록한 날짜, 오늘의 점수, 감사한/배운 일 내용 정보 리스트 (null 가능)
     */
    Optional<List<DateContents>> findSortedContentDetailInMonth(Long memberId, ContentType type, Integer year, Integer month, SortType orderBy);
}
