package com.soothee.dairy.repository;

import com.soothee.dairy.domain.DairyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DairyConditionRepository extends JpaRepository<DairyCondition, Long> {
    /**
     * 다이어리 일련번호로 다이어리-컨디션 조회</hr>
     *
     * @param dairyId Long : 조회할 다이어리 일련번호
     * @param isDelete String : "Y" 삭제한 일기 제외
     * @return Optional<List<DairyCondition>> : 조회된 다이어리의 다이어리-컨디션 리스트 (null 가능)
     */
    Optional<List<DairyCondition>> findByDairyDairyIdAndIsDelete(Long dairyId, String isDelete);
}
