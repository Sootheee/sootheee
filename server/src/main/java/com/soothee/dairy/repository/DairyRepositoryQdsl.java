package com.soothee.dairy.repository;

import com.soothee.dairy.dto.DairyScoresDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DairyRepositoryQdsl {
    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회</hr>
     *
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year Integer : 지정한 년도
     * @param month Integer : 지정한 월
     * @return Optional<List<DairyScoresDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트 (null 가능)
     */
    Optional<List<DairyScoresDTO>> findByMemberIdAndDate(@Param("member_id") Long memberId,
                                                         @Param("year") Integer year,
                                                         @Param("month") Integer month);
}
