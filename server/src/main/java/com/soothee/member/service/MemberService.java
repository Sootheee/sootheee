package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NotExistMemberException;
import com.soothee.custom.exception.NotMatchedException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.oauth2.domain.AuthenticatedUser;

import java.util.Optional;

public interface MemberService {
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
     * 회원 가입
     *
     * @param member 가입할 회원의 정보
     */
    void saveMember(Member member);

    /**
     * 회원 닉네임 수정
     * - 삭제한 회원 제외
     *
     * @param loginMemberId 현재 로그인한 계정의 일련번호
     * @param memberId 입력한 회원 일련번호
     * @param updateMode 변경할 모드
     */
    void updateName(Long loginMemberId, Long memberId, String updateMode) throws NotExistMemberException, IncorrectValueException, NullValueException, NotMatchedException;

    /**
     * 회원 다크모드 수정
     * - 삭제한 회원 제외
     *
     * @param loginMemberId 현재 로그인한 계정의 일련번호
     * @param memberId 입력한 회원 일련번호
     * @param isDark 다크모드 yes/no
     */
    void updateDarkMode(Long loginMemberId, Long memberId, String updateMode) throws NotExistMemberException, IncorrectValueException, NullValueException, NotMatchedException;

    /**
     * 회원 탈퇴
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @param memberDelDTO 입력된 탈퇴 정보
     */
    void deleteMember(Long memberId, MemberDelDTO memberDelDTO) throws NotExistMemberException, NullValueException, NotMatchedException, IncorrectValueException;

    /**
     * 로그인한 회원의 모든 정보 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @return 회원의 모든 정보
     */
    MemberInfoDTO getAllMemberInfo(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException;

    /**
     * 로그인한 회원의 닉네임만 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 현재 로그인한 계정의 일련번호
     * @return 회원 일련번호와 닉네임 정보
     */
    MemberNameDTO getNicknameInfo(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException;

    /**
     * 현재 로그인한 회원의 인증 일련번호로 회원 일련번호 조회
     * - 삭제한 회원 제외
     *
     * @param loginInfo 현재 로그인 계정 정보
     * @return 로그인한 회원의 일련번호
     */
    Long getLoginMemberId(AuthenticatedUser loginInfo) throws NotExistMemberException;

    /**
     * 회원 일련번호로 회원 정보 조회
     * - 삭제한 회원 제외
     *
     * @param memberId 조회할 회원 일련번호
     * @return 조회한 회원 정보
     */
    Member getMemberById(Long memberId) throws NotExistMemberException;
}
