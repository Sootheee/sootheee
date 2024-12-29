package com.soothee.dairy.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.dairy.domain.DairyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyConditionRepository extends JpaRepository<DairyCondition, Long>, QuerydslPredicateExecutor<DairyCondition>, DairyConditionRepositoryQdsl {

    /**
     * 일기 일련번호로 일기의 컨디션 리스트가 존재하는지 확인
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @param dairyDelete BooleanYN.N 삭제한 일기 제외
     * @param isDelete BooleanYN.N 삭제한 일기 제외
     * @return 존재하면 true 아니면 false
     */
    boolean existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(Long dairyId, BooleanYN dairyDelete, BooleanYN isDelete);
}
