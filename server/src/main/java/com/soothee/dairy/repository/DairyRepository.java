package com.soothee.dairy.repository;

import com.soothee.dairy.domain.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DairyRepository extends JpaRepository<Dairy, Long>, QuerydslPredicateExecutor<Dairy>, DairyRepositoryQdsl {
    /**
     * 회원 일련번호로 회원이 작성한 모든 일기 조회</hr>
     *
     * @param memberId Long : 회원 일련번호
     * @param isDelete String : "Y" 삭제된 일기 제외
     * @return Optional<List<Dairy>> : 회원이 작성한 모든 일기 리스트 (null 가능)
     */
    Optional<List<Dairy>> findByMemberMemberIdAndIsDelete(Long memberId, String isDelete);

    /**
     * 일기 일련번호로 해당 일기 정보 조회</hr>
     *
     * @param dairyId Long : 일기 일련번호
     * @return Optional<Dairy> : 조회된 일기 정보 (null 가능)
     */
    Optional<Dairy> findByDairyId(Long dairyId);
}
