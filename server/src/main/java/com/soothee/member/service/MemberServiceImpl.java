package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.SnsType;
import com.soothee.custom.exception.*;
import com.soothee.member.domain.Member;
import com.soothee.member.controller.response.MemberAllInfoResponse;
import com.soothee.member.controller.response.MemberNameResponse;
import com.soothee.member.repository.MemberRepository;
import com.soothee.member.service.command.MemberDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.soothee.custom.valid.SootheeValidation.checkAuthorizedMember;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberDelReasonService memberDelReasonService;

    @Override
    public MemberAllInfoResponse getAllMemberInfo(Long memberId) throws NotExistMemberException {
        Member member = getMemberById(memberId);
        return MemberAllInfoResponse.fromMember(member);
    }

    @Override
    public MemberNameResponse getNicknameInfo(Long memberId) throws NotExistMemberException {
        Member member = getMemberById(memberId);
        return MemberNameResponse.fromMember(member);
    }

    @Override
    public void updateDarkMode(Long loginMemberId, Long memberId, BooleanYN isDark) throws NotExistMemberException, NoAuthorizeException {
        Member loginMember = getMemberById(loginMemberId);
        checkAuthorizedMember(loginMemberId, memberId);
        loginMember.updateDarkModeYN(isDark);
    }

    @Override
    public void updateName(Long loginMemberId, Long memberId, String updateName) throws NoAuthorizeException, NotExistMemberException {
        checkAuthorizedMember(loginMemberId, memberId);
        Member loginMember = getMemberById(loginMemberId);
        loginMember.updateName(updateName);
    }

    @Override
    public void deleteMember(Long loginMemberId, MemberDelete deleteInfo) throws NullValueException, NotExistMemberException, NoAuthorizeException {
        Member loginMember = getMemberById(deleteInfo.getMemberId());
        checkAuthorizedMember(loginMemberId, loginMember.getMemberId());
        memberDelReasonService.saveDeleteReasons(loginMember, deleteInfo.getDelReasonIdList());
        loginMember.deleteMember();
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        return memberRepository.findByOauth2ClientIdAndSnsTypeAndIsDelete(oauth2ClientId, snsType, BooleanYN.N);
    }

    @Override
    public Member getMemberById(Long memberId) throws NotExistMemberException {
        return memberRepository.findByMemberIdAndIsDelete(memberId, BooleanYN.N)
                .orElseThrow(() -> new NotExistMemberException(memberId));
    }
}
