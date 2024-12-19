package com.soothee.dairy.repository;

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
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId      Long : 로그인한 계정 일련번호
     * @param monthParam    MonthParam : 지정한 년도/달
     * @return Optional<List<DairyScoresDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId, MonthParam monthParam);

    /**
     * 로그인한 계정이 지정한 날에 작성한 일기 정보 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param date     LocalDate : 지정한 날짜
     * @return Optional<DairyDTO> : 조회된 일기 모든 정보 (null 가능)
     */
    Optional<DairyDTO> findByDate(Long memberId, LocalDate date);

    /**
     * 로그인한 계정이 작성한 해당 일련번호의 일기 정보 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param dairyId  Long : 조회할 일기 일련번호
     * @return Optional<DairyDTO> : 조회된 일기 모든 정보 (null 가능)
     */
    Optional<DairyDTO> findByMemberDiaryId(Long memberId, Long dairyId);

    /**
     * 로그인한 계정이 지정한 년도/달에 작성한 일기 개수 & 오늘의 점수 평균 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param monthParam   MonthParam : 지정한 년도/달
     * @return Optional<MonthlyStatsDTO> : 지정한 년도/달 동안 작성한 일기 개수와 오늘의 점수 평균 (null 가능)
     */
    Optional<MonthlyStatsDTO> findDiaryStatsInMonth(Long memberId, MonthParam monthParam);

    /**
     * 해당 달에 작성한 고마운/배운 일 횟수</hr>
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param type     String : 고마운/배운 일 중 타입
     * @param monthParam   MonthParam : 지정한 년도/달
     * @return Optional<Integer> :
     */
    Optional<Integer> findDiaryContentCntInMonth(Long memberId, String type, MonthParam monthParam);

    /**
     * 해당 달에 작성한 고마운/배운 일 중 가장 높은/낮은 점수를 기록한 날의 고마운/배운 일</hr>
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param type     String : 고마운/배운 일 중 타입
     * @param monthParam   MonthParam : 지정한 년도/달
     * @param high     String : 가장 높은/낮은 점수 중 타입
     * @return Optional<DateContents>
     */
    Optional<DateContents> findDiaryContentInMonth(Long memberId, String type, MonthParam monthParam, String high);

    /**
     * 로그인한 계정이 지정한 년도/주차에 작성한 일기 개수 & 오늘의 점수 평균 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param weekParam   WeekParam : 지정한 년도/주차
     * @return Optional<WeeklyStatsDTO> : 지정한 년도/주차 동안 작성한 일기 개수와 오늘의 점수 평균 (null 가능)
     */
    Optional<WeeklyStatsDTO> findDiaryStatsInWeekly(Long memberId, WeekParam weekParam);

    /**
     * 로그인한 계정이 지정한 년도/주차에 작성한 일기 날짜와 점수 리스트</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param weekParam   WeekParam : 지정한 년도/주차
     * @return Optional<List<DateScore>> : 지정한 년도/주차 동안 작성한 일기 날짜와 점수 리스트 (null 가능)
     */
    Optional<List<DateScore>> findDiaryScoresInWeekly(Long memberId, WeekParam weekParam);
}
