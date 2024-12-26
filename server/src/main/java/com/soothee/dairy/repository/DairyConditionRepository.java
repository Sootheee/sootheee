package com.soothee.dairy.repository;

import com.soothee.dairy.domain.DairyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DairyConditionRepository extends JpaRepository<DairyCondition, Long>, QuerydslPredicateExecutor<DairyCondition>, DairyConditionRepositoryQdsl {
    /**
     * 일기 일련번호로 일기의 컨디션 리스트 조회
     * 일기 컨디션의 선택 순서대로 조회됨
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @param dairyDelete "N" 삭제한 일기 제외
     * @param isDelete "N" 삭제한 일기 제외
     * @return 조회된 일기의 일기-컨디션 리스트 선택한 순서대로 (null 가능)
     */
    Optional<List<DairyCondition>> findByDairyDairyIdAndDairyIsDeleteAndIsDeleteOrderByOrderNoAsc(Long dairyId, String dairyDelete, String isDelete);

    /**
     * 일기 일련번호로 일기의 컨디션 리스트가 존재하는지 확인
     * - 삭제한 일기 제외
     * - 삭제한 일기-컨디션 제외
     *
     * @param dairyId 조회할 일기 일련번호
     * @param dairyDelete "N" 삭제한 일기 제외
     * @param isDelete "N" 삭제한 일기 제외
     * @return 존재하면 true 아니면 false
     */
    boolean existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(Long dairyId, String dairyDelete, String isDelete);
}
