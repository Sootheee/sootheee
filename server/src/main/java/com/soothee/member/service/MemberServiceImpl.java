package com.soothee.member.service;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NotExistMemberException;
import com.soothee.custom.exception.NotMatchedException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.member.repository.MemberRepository;
import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberDelReasonService memberDelReasonService;

    @Override
    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        return memberRepository.findByOauth2ClientIdAndSnsTypeAndIsDelete(oauth2ClientId, snsType, "N");
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void updateName(Long loginMemberId, Long memberId, String updateName) throws NotExistMemberException, IncorrectValueException, NullValueException, NotMatchedException {
        Member loginMember = this.getMemberById(loginMemberId);
        this.isNotLoginMemberInfo(loginMember, memberId);
        loginMember.updateName(updateName);
    }

    @Override
    public void updateDarkMode(Long loginMemberId, Long memberId, String updateMode) throws NotExistMemberException, IncorrectValueException, NullValueException, NotMatchedException {
        Member loginMember = this.getMemberById(loginMemberId);
        this.isNotLoginMemberInfo(loginMember, memberId);
        loginMember.updateDarkModeYN(updateMode);
    }

    @Override
    public void deleteMember(Long memberId, MemberDelDTO memberDelDTO) throws NotExistMemberException, NotMatchedException, NullValueException, IncorrectValueException {
        Member loginMember =  this.getMemberById(memberId);
        this.isNotLoginMemberInfo(loginMember, memberDelDTO.getMemberId());
        memberDelReasonService.saveDeleteReasons(loginMember, memberDelDTO.getDelReasonList());
        loginMember.deleteMember();
    }

    @Override
    public MemberInfoDTO getAllMemberInfo(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException {
        Member member = this.getMemberById(memberId);
        return MemberInfoDTO.fromMember(member);
    }

    @Override
    public MemberNameDTO getNicknameInfo(Long memberId) throws NotExistMemberException, IncorrectValueException, NullValueException {
        Member member = this.getMemberById(memberId);
        return MemberNameDTO.fromMember(member);
    }

    @Override
    public Long getLoginMemberId(AuthenticatedUser loginInfo) throws NotExistMemberException {
        String oauth2Id = loginInfo.getName();
        return memberRepository.findByOauth2ClientIdAndIsDelete(oauth2Id, "N")
                .orElseThrow(() -> new NotExistMemberException(loginInfo)).getMemberId();
    }

    @Override
    public Member getMemberById(Long memberId) throws NotExistMemberException {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotExistMemberException(memberId));
    }

    /**
     * 로그인한 회원의 정보와 입력된 회원의 정보가 일치하지 않는지 확인
     *
     * @param loginInfo 로그인한 회원 정보
     * @param inputMemberId 입력한 회원 일련번호
     */
    private void isNotLoginMemberInfo(Member loginInfo, Long inputMemberId) throws NotMatchedException {
        if (!Objects.equals(loginInfo.getMemberId(), inputMemberId)) {
            throw new NotMatchedException(loginInfo.getMemberId(), inputMemberId, DomainType.MEMBER);
        }
    }
}
