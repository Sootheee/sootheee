package com.soothee.dairy.repository;

import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.MonthlyDairyScoreDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DairyRepository extends JpaRepository<Dairy, Long> {

    /**
     * 로그인한 계정이 지정한 년도-월에 작성한 일기 정보 조회
     * @param memberId Long : 로그인한 계정 일련번호
     * @param year Integer : 지정한 년도
     * @param month Integer : 지정한 월
     * @return Optional<List<MonthlyDairyScoreDTO>> : 조회된 (일기 일련번호, 일기 날짜, 오늘의 점수) 정보 리스트
     */
    @Query(value = "SELECT new com.soothee.dairy.dto.MonthlyDairyScoreDTO(dairy.id, dairy.date, dairy.score) " +
            "FROM Dairy dairy " +
            "WHERE dairy.id = :member_id " +
            "AND YEAR(dairy.date) = :year " +
            "AND MONTH(dairy.date) = :month")
    Optional<List<MonthlyDairyScoreDTO>> findByMemberIdAndDate(@Param("member_id") Long memberId,
                                                               @Param("year") Integer year,
                                                               @Param("month") Integer month);

}
