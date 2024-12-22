package com.soothee.reference.repository;

import com.soothee.reference.domain.DelReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DelReasonRepository extends JpaRepository<DelReason, Long> {
    /**
     * 탈퇴 사유 일련번호로 탈퇴 사유 조회
     *
     * @param reasonId 탈퇴 사유 일련번호
     * @return 조회된 탈퇴 사유 (null 가능)
     */
    Optional<DelReason> findByReasonId(Long reasonId);
}
