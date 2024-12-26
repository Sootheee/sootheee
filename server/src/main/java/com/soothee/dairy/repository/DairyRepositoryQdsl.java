package com.soothee.dairy.repository;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.stats.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DairyRepositoryQdsl {
    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param monthParam 지정한 년도/달
     * @return 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId, MonthParam monthParam);

    /**
     * 로그인한 계정이 지정한 날에 작성한 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param date     지정한 날짜
     * @return 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyDTO>> findByDate(Long memberId, LocalDate date);

    /**
     * 로그인한 계정이 작성한 해당 일련번호의 일기 정보 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param dairyId  조회할 일기 일련번호
     * @return 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyDTO>> findByMemberDiaryId(Long memberId, Long dairyId)  ;

    /**
     * 로그인한 계정이 지정한 년도/달에 작성한 일기 갯수 & 오늘의 점수 평균 조회
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param monthParam 지정한 년도/달
     * @return 지정한 년도/달 동안 작성한 일기 갯수와 오늘의 점수 평균 (null 가능)
     */
    Optional<List<MonthlyStatsDTO>> findDiaryStatsInMonth(Long memberId, MonthParam monthParam)  ;

    /**
     * 해당 달에 작성한 감사한/배운 일 횟수
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param type       감사한/배운 일 중 타입
     * @param monthParam 지정한 년도/달
     * @return 작성한 감사한/배운 일 횟수 (null 가능)
     */
    Integer findDiaryContentCntInMonth(Long memberId, ContentType type, MonthParam monthParam);

    /**
     * 해당 달에 작성한 감사한/배운 일 중 가장 높은/낮은 점수를 기록한 날의 감사한/배운 일
     * - 삭제한 일기 제외
     *
     * @param memberId   로그인한 계정 일련번호
     * @param type       감사한/배운 일 중 타입
     * @param monthParam 지정한 년도/달
     * @param sort       가장 높은/낮은 점수 중 타입
     * @return 기록한 날짜, 가장 높은/낮은 점수, 감사한/배운 일 내용 정보 리스트 (null 가능)
     */
    Optional<List<DateContents>> findDiaryContentInMonthHL(Long memberId, ContentType type, MonthParam monthParam, SortType sort)  ;

    /**
     * 로그인한 계정이 지정한 년도/주차에 작성한 일기 갯수 & 오늘의 점수 평균 조회
     * - 삭제한 일기 제외
     *
     * @param memberId  로그인한 계정 일련번호
     * @param weekParam 지정한 년도/주차
     * @return 지정한 년도/주차 동안 작성한 일기 갯수와 오늘의 점수 평균 (null 가능)
     */
    Optional<List<WeeklyStatsDTO>> findDiaryStatsInWeekly(Long memberId, WeekParam weekParam)  ;

    /**
     * 로그인한 계정이 지정한 년도/주차에 작성한 일기 날짜와 점수 리스트
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param weekParam 지정한 년도/주차
     * @return 지정한 년도/주차 동안 작성한 일기 날짜와 점수 리스트 (null 가능)
     */
    Optional<List<DateScore>> findDiaryScoresInWeekly(Long memberId, WeekParam weekParam)  ;

    /**
     * 해당 달에 작성한 모든 감사한/배운 일 정보 리스트 조회
     * - 삭제한 일기 제외
     *
     * @param memberId 로그인한 계정 일련번호
     * @param type 감사한/배운 일 중 타입
     * @param monthParam 지정한 년도/달
     * @param orderBy 조회 순서 타입
     * @return 기록한 날짜, 오늘의 점수, 감사한/배운 일 내용 정보 리스트 (null 가능)
     */
    Optional<List<DateContents>> findDiaryContentInMonthSort(Long memberId, ContentType type, MonthParam monthParam, SortType orderBy)  ;
}
