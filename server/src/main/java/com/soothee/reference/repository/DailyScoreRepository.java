package com.soothee.reference.repository;

import com.soothee.reference.domain.DailyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyScoreRepository extends JpaRepository<DailyScore, Long> {
}
