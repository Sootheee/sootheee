package com.soothee.member.repository;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 회원 닉네임으로 회원 조회
     * @param memberName String : 조회할 회원 닉네임
     * @return Member : 해당 회원
     */
    Member findByMemberName(String memberName);

    Optional<Member> findByOauth2ClientIdAndSnsType(String oauth2ClientId, SnsType snsType);
}
