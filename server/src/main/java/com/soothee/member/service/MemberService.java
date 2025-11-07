package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.*;
import com.soothee.member.domain.Member;
import com.soothee.member.controller.response.MemberAllInfoResponse;
import com.soothee.member.controller.response.MemberNameResponse;
import com.soothee.member.service.command.MemberDelete;

import java.util.Optional;

public interface MemberService {
    /**
     * 로그인한 회원의 모든 정보 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @return 회원의 모든 정보
     */
    MemberAllInfoResponse getAllMemberInfo(Long memberId) throws NotExistMemberException;

    /**
     * 로그인한 회원의 닉네임만 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @return 회원 일련번호와 닉네임 정보
     */
    MemberNameResponse getNicknameInfo(Long memberId) throws NotExistMemberException;

    /**
     * 회원 닉네임 수정
     * - 삭제한 회원 제외
     *
     * @param loginMemberId 현재 로그인한 계정의 일련번호
     * @param memberId 입력한 회원 일련번호
     * @param updateMode 변경할 모드
     */
    void updateName(Long loginMemberId, Long memberId, String updateMode) throws NoAuthorizeException, NotExistMemberException;

    /**
     * 회원 다크모드 수정
     * - 삭제한 회원 제외
     *
     * @param loginMemberId 현재 로그인한 계정의 일련번호
     * @param memberId 입력한 회원 일련번호
     * @param isDark 다크모드 yes/no
     */
    void updateDarkMode(Long loginMemberId, Long memberId, BooleanYN isDark) throws NoAuthorizeException, NotExistMemberException;

    /**
     * 회원 탈퇴
     *
     * @param loginMemberId 현재 로그인한 계정의 일련번호
     * @param deleteInfo 입력된 탈퇴 정보
     */
    void deleteMember(Long loginMemberId, MemberDelete deleteInfo) throws NoAuthorizeException, NullValueException, NotExistMemberException;

    /**
     * 회원 가입
     *
     * @param member 가입할 회원의 정보
     */
    void saveMember(Member member);

    /**
     * OAuth2 로그인 시, 받은 정보로 회원 조회
     * - 삭제한 회원 제외
     *
     * @param oauth2ClientId OAuth2 로그인용 ID
     * @param snsType OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
     * @return 해당 정보의 맴버(Null 일 수도 있음)
     */
    Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType);

    /**
     * 회원 일련번호로 회원 정보 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 조회할 회원 일련번호
     * @return 조회한 회원 정보
     */
    Member getMemberById(Long memberId) throws NotExistMemberException;
}
