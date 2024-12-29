package com.soothee.member.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 인증 회원 식별자와 SNS 종류로 회원 조회
     * - 삭제한 회원 제외
     *
     * @param oauth2ClientId 인증 회원 식별자
     * @param snsType KAKAOTALK or GOOGLE
     * @param isDelete BooleanYN.N 삭제한 회원 제외
     * @return 조회한 회원(Null 가능)
     */
    Optional<Member> findByOauth2ClientIdAndSnsTypeAndIsDelete(String oauth2ClientId, SnsType snsType, BooleanYN isDelete);

    /**
     * 인증 회원 식별자로 회원
     * - 삭제한 회원 제외
     *
     * @param oauth2ClientId 인증 회원 식별자
     * @param isDelete BooleanYN.N 삭제한 회원 제외
     * @return 조회한 회원(Null 가능)
     */
    Optional<Member> findByOauth2ClientIdAndIsDelete(String oauth2ClientId, BooleanYN isDelete);

    /**
     * 회원 이메일로 회원 조회
     * - 삭제한 회원 제외
     *
     * @param email 조회할 회원 이메일
     * @param isDelete BooleanYN.N 삭제한 회원 제외
     * @return 조회한 회원(Null 가능)
     */
    Optional<Member> findByEmailAndIsDelete(String email, BooleanYN isDelete);

    /**
     * 회원 일련번호로 회원 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 조회할 회원 일련번호
     * @param isDelete BooleanYNN 삭제한 회원 제외
     * @return 조회한 회원(Null 가능)
     */
    Optional<Member> findByMemberIdAndIsDelete(Long memberId, BooleanYN isDelete);
}
