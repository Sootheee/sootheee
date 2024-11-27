package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.AllMemberInfoDTO;
import com.soothee.member.dto.NameMemberInfoDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;

import java.util.Optional;

public interface MemberService {
    /**
     * OAuth2 로그인 시, 받은 정보로 회원 조회</hr>
     *
     * @param oauth2ClientId String : OAuth2 로그인용 ID
     * @param snsType SnsType : OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
     * @return Optional<Member> : 해당 정보의 맴버(Null 일 수도 있음)
     */
    Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType);

    /**
     * 회원 가입</hr>
     *
     * @param member Member : 가입할 회원의 정보
     */
    void saveMember(Member member);

    /**
     * 회원 닉네임 수정</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @param memberId Long : 입력한 회원 일련번호
     * @param updateMode String : 변경할 모드
     */
    void updateMember(AuthenticatedUser loginInfo, Long memberId, String updateMode);

    /**
     * 회원 다크모드 수정</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @param memberId Long : 입력한 회원 일련번호
     * @param updateMode String : 변경할 모드
     */
    void updateDarkMode(AuthenticatedUser loginInfo, Long memberId, String updateMode);

    /**
     * 회원 탈퇴</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @param memberId Long : 입력한 회원 일련번호
     */
    void deleteMember(AuthenticatedUser loginInfo, Long memberId);

    /**
     * 마이페이지에 보여질 회원의 정보 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @return AllMemberInfo : 회원의 모든 정보
     */
    AllMemberInfoDTO getAllMemberInfo(AuthenticatedUser loginInfo);

    /**
     * 회원의 닉네임만 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @return NameMemberInfoDTO : 회원 일련번호와 닉네임 정보
     */
    NameMemberInfoDTO getNicknameInfo(AuthenticatedUser loginInfo);

}
