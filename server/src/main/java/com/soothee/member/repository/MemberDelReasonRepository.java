package com.soothee.member.repository;

import com.soothee.member.domain.MemberDelReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberDelReasonRepository extends JpaRepository<MemberDelReason, Long> {
    /**
     * 회원 일련번호로 탈퇴 사유 리스트 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 조회할 회원 일련번호=
     * @return 조회된 회원 탈퇴 사유 리스트 (null 가능)
     */
    Optional<List<MemberDelReason>> findByMemberMemberId(Long memberId);
}
