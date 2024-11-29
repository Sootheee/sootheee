package com.soothee.dairy.repository;

import com.soothee.dairy.domain.DairyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyConditionRepository extends JpaRepository<DairyCondition, Long> {

}
