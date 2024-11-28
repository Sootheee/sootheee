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

    @Query(value = "SELECT new com.soothee.dairy.dto.MonthlyDairyScoreDTO(dairy.id, dairy.date, dairy.score) " +
            "FROM Dairy dairy " +
            "WHERE dairy.id = :member_id " +
            "AND YEAR(dairy.date) = :year " +
            "AND MONTH(dairy.date) = :month")
    Optional<List<MonthlyDairyScoreDTO>> findByMemberIdAndDate(@Param("member_id") Long memberId,
                                                               @Param("year") Integer year,
                                                               @Param("month") Integer month);

}
