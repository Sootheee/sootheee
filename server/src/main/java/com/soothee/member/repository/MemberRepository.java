package com.soothee.member.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 회원 이메일로 회원 조회</hr>
     *
     * @param email String : 조회할 회원 이메일
     * @return Optional<Member> : 조회한 회원(Null 가능)
     */
    Optional<Member> findByEmail(String email);

    /**
     * 인증 회원 식별자와 SNS 종류로 회원 조회</hr>
     *
     * @param oauth2ClientId String : 인증 회원 식별자
     * @param snsType SnsType : KAKAOTALK or GOOGLE
     * @return Optional<Member> : 조회한 회원(Null 가능)
     */
    Optional<Member> findByOauth2ClientIdAndSnsType(String oauth2ClientId, SnsType snsType);

    /**
     * 인증 회원 식별자로 회원</hr>
     *
     * @param oauth2ClientId String : 인증 회원 식별자
     * @return Optional<Member> : 조회한 회원(Null 가능)
     */
    Optional<Member> findByOauth2ClientId(String oauth2ClientId);

    /**
     * 회원 일련번호로 회원 조회</hr>
     *
     * @param memberId Long : 조회할 회원 일련번호
     * @return Optional<Member> : 조회한 회원(Null 가능)
     */
    Optional<Member> findByMemberId(Long memberId);
}
