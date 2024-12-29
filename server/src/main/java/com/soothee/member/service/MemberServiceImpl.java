package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NotExistMemberException;
import com.soothee.custom.exception.NotMatchedException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.member.repository.MemberRepository;
import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberDelReasonService memberDelReasonService;

    @Override
    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        /* OAuth2 인증 일련번호와 가입한 SNS 정보로 회원 정보 조회 - null 가능 */
        return memberRepository.findByOauth2ClientIdAndSnsTypeAndIsDelete(oauth2ClientId, snsType, BooleanYN.N.toString());
    }

    @Override
    public void saveMember(Member member) throws IncorrectValueException, NullValueException {
        member.validNew();
        /* 회원 등록 */
        memberRepository.save(member);
    }

    @Override
    public void updateDarkMode(Long loginMemberId, Long memberId, BooleanYN isDark) throws NotExistMemberException, NoAuthorizeException {
        /* 회원 일련번호로 회원 정보 조회
         * - 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member loginMember = getMemberById(loginMemberId);
        /* 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하는지 확인
        loginMember.updateDarkModeYN(isDark);
        SootheeValidation.checkMatchedId(loginMemberId, memberId, DomainType.MEMBER);
        /* 닉네임 수정
         * - 입력된 수정할 닉네임이 없거나 올바르지 않는 경우 Exception 발생*/
        loginMember.updateName(updateName);
    }

    @Override
    public void updateDarkMode(Long loginMemberId, Long memberId, BooleanYN isDark) throws NotExistMemberException, NotMatchedException, IncorrectValueException, NullValueException {
        /* 회원 일련번호로 회원 정보 조회
         * - 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member loginMember = getMemberById(loginMemberId);
        /* 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하는지 확인
         * - 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하지 않는 경우 Exception 발생 */
        SootheeValidation.checkMatchedId(loginMemberId, memberId, DomainType.MEMBER);
        /* 다크 모드 수정
         * 입력된 다크 모드가 없거나 올바르지 않는 경우 Exception 발생 */
        loginMember.updateDarkModeYN(isDark);
    }

    @Override
    public void deleteMember(Long memberId, MemberDelDTO memberDelDTO) throws NotExistMemberException, NotMatchedException, IncorrectValueException, NullValueException {
        /* 회원 일련번호로 회원 정보 조회
         * - 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member loginMember =  getMemberById(memberId);

        /* 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하는지 확인
         * - 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하지 않는 경우 Exception 발생 */
        SootheeValidation.checkMatchedId(loginMember.getMemberId(), memberId, DomainType.MEMBER);

        /* 입력된 회원 탈퇴 사유 등록
         * - 탈퇴 사유 일련번호로 조회된 탈퇴 사유가 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        memberDelReasonService.saveDeleteReasons(loginMember, memberDelDTO.getDelReasonIdList());

        /* 회원 소프트 삭제 */
        loginMember.deleteMember();
    }

    @Override
    public MemberInfoDTO getAllMemberInfo(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException {
        /* 회원 일련번호로 회원 정보 조회
         * - 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member member = getMemberById(memberId);
        /* 조회된 회원 정보를 response DTO 변형
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        return MemberInfoDTO.fromMember(member);
    }

    @Override
    public MemberNameDTO getNicknameInfo(Long memberId) throws IncorrectValueException, NullValueException, NotExistMemberException {
        /* 회원 일련번호로 회원 정보 조회
         * - 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        Member member = getMemberById(memberId);
        /* 조회된 회원 정보를 response DTO 변형
         * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        return MemberNameDTO.fromMember(member);
    }

    @Override
    public Member getMemberById(Long memberId) throws NotExistMemberException {
        return memberRepository.findByMemberIdAndIsDelete(memberId, BooleanYN.N)
    @Override
    public Member getMemberById(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException {
        /* 회원 일련번호로 회원 정보 조회 */
        Member result = memberRepository.findByMemberIdAndIsDelete(memberId, BooleanYN.N.toString())
                /* 회원 일련번호로 조회된 회원이 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NotExistMemberException(memberId));
        /* 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
        result.valid();
        return result;
    }
}
