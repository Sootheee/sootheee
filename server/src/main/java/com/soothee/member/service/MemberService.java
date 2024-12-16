package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;

import java.util.Optional;

public interface MemberService {
    /**
     * OAuth2 로그인 시, 받은 정보로 회원 조회</hr>
     *
     * @param oauth2ClientId String : OAuth2 로그인용 ID
     * @param snsType        SnsType : OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
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
     * @param loginMemberId Long : 현재 로그인한 계정의 일련번호
     * @param memberId   Long : 입력한 회원 일련번호
     * @param updateMode String : 변경할 모드
     */
    void updateName(Long loginMemberId, Long memberId, String updateMode);

    /**
     * 회원 다크모드 수정</hr>
     *
     * @param loginMemberId Long : 현재 로그인한 계정의 일련번호
     * @param memberId   Long : 입력한 회원 일련번호
     * @param updateMode String : 변경할 모드
     */
    void updateDarkMode(Long loginMemberId, Long memberId, String updateMode);

    /**
     * 회원 탈퇴</hr>
     *
     * @param memberId Long : 현재 로그인한 계정의 일련번호
     * @param memberDelDTO  MemberDelDTO : 입력된 탈퇴 정보
     */
    void deleteMember(Long memberId, MemberDelDTO memberDelDTO);

    /**
     * 로그인한 회원의 모든 정보 조회</hr>
     *
     * @param memberId Long : 현재 로그인한 계정의 일련번호
     * @return MemberInfoDTO : 회원의 모든 정보
     */
    MemberInfoDTO getAllMemberInfo(Long memberId);

    /**
     * 로그인한 회원의 닉네임만 조회</hr>
     *
     * @param memberId Long : 현재 로그인한 계정의 일련번호
     * @return MemberNameDTO : 회원 일련번호와 닉네임 정보
     */
    MemberNameDTO getNicknameInfo(Long memberId);

    /**
     * 현재 로그인한 회원의 일련번호 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인 계정 정보
     * @return String : 로그인한 회원의 일련번호
     */
    Long getLoginMemberId(AuthenticatedUser loginInfo);

    /**
     * 현재 로그인한 회원 정보 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인 계정 정보
     * @return Member: 로그인한 회원의 정보
     */
    Member getLoginMember(AuthenticatedUser loginInfo);

    /**
     * 회원 일련번호로 회원 정보 조회</hr>
     *
     * @param memberId Long : 조회할 회원 일련번호
     * @return Member : 조회한 회원 정보
     */
    Member getMemberById(Long memberId);
}
