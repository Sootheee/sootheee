package com.soothee.reference.repository;

import com.soothee.reference.domain.ConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionTypeRepository extends JpaRepository<ConditionType, Long> {
}
