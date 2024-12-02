package com.soothee.reference.repository;

import com.soothee.reference.domain.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    /**
     * 컨디션 일련번호로 컨디션 조회</hr>
     *
     * @param condId Long : 해당 컨디션 일련번호
     * @return Optional<Condition> : 조회한 컨디션 정보 (null 가능)
     */
    Optional<Condition> findByCondId(Long condId);
}
