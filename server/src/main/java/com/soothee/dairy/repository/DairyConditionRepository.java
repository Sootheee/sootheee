package com.soothee.dairy.repository;

import com.soothee.dairy.domain.DairyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DairyConditionRepository extends JpaRepository<DairyCondition, Long> {
    /**
     * 일기 일련번호로 일기의 컨디션 리스트 조회</hr>
     * 일기 컨디션의 선택 순서대로 조회됨
     *
     * @param dairyId Long : 조회할 일기 일련번호
     * @param isDelete String : "Y" 삭제한 일기 제외
     * @return Optional<List<DairyCondition>> : 조회된 일기의 일기-컨디션 리스트 선택한 순서대로 (null 가능)
     */
    Optional<List<DairyCondition>> findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(Long dairyId, String isDelete);
}
