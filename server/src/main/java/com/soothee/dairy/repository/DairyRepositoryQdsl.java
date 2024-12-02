package com.soothee.dairy.repository;

import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyScoresDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DairyRepositoryQdsl {
    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회</hr>
     * 삭제한 일기 제외
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year Integer : 지정한 년도
     * @param month Integer : 지정한 월
     * @return Optional<List<DairyScoresDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    Optional<List<DairyScoresDTO>> findByMemberIdYearMonth(Long memberId,Integer year,Integer month);

    /**
     * 로그인한 계정이 지정한 날에 작성한 일기 정보 조회</hr>
     * 삭제한 일기 제외
     * @param memberId Long : 로그인한 계정 일련번호
     * @param date LocalDate : 지정한 날짜
     * @return Optional<DairyDTO> : 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyDTO>> findByDate(Long memberId, LocalDate date);

    /**
     * 로그인한 계정이 작성한 해당 일련번호의 일기 정보 조회</hr>
     * 삭제한 일기 제외
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param dairyId  Long : 조회할 일기 일련번호
     * @return Optional<List<DairyDTO>> : 조회된 일기 모든 정보 (null 가능)
     */
    Optional<List<DairyDTO>> findByDiaryId(Long memberId, Long dairyId);
}
