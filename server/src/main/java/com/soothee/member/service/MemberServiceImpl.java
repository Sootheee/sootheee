package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.MemberDelDTO;
import com.soothee.member.dto.MemberInfoDTO;
import com.soothee.member.dto.MemberNameDTO;
import com.soothee.member.repository.MemberRepository;
import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
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
    public void updateName(Long loginMemberId, Long memberId, String updateName) {
        Member loginMember = this.getMemberById(loginMemberId);
        if (this.isNotLoginMemberInfo(loginMember, memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        loginMember.updateName(updateName);
    }

    @Override
    public void updateDarkMode(Long loginMemberId, Long memberId, String updateMode) {
        Member loginMember = this.getMemberById(loginMemberId);
        if (this.isNotLoginMemberInfo(loginMember, memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        loginMember.updateDarkModeYN(updateMode);
    }

    @Override
    public void deleteMember(Long memberId, MemberDelDTO memberDelDTO) {
        Member loginMember =  this.getMemberById(memberId);
        if (this.isNotLoginMemberInfo(loginMember, memberDelDTO.getMemberId())) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        memberDelReasonService.saveDeleteReasons(loginMember, memberDelDTO.getDelReasonList());
        loginMember.deleteMember();
    }

    @Override
    public MemberInfoDTO getAllMemberInfo(Long memberId) {
        Member member = this.getMemberById(memberId);
        return MemberInfoDTO.fromMember(member);
    }

    @Override
    public MemberNameDTO getNicknameInfo(Long memberId) {
        Member member = this.getMemberById(memberId);
        return MemberNameDTO.fromMember(member);
    }

    @Override
    public Long getLoginMemberId(AuthenticatedUser loginInfo) {
        return this.getLoginMember(loginInfo).getMemberId();
    }

    @Override
    public Member getLoginMember(AuthenticatedUser loginInfo) {
        String oauth2Id = loginInfo.getName();
        return memberRepository.findByOauth2ClientIdAndIsDelete(oauth2Id, "N")
                .orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.NOT_EXIST_MEMBER));
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.NOT_EXIST_MEMBER));
    }

    /**
     * 로그인한 회원의 정보와 입력된 회원의 정보가 일치하지 않는지 확인
     *
     * @param loginInfo 로그인한 회원 정보
     * @param inputMemberId 입력한 회원 일련번호
     * @return 일치하면 false, 아니면 true
     */
    private boolean isNotLoginMemberInfo(Member loginInfo, Long inputMemberId) {
        return !Objects.equals(loginInfo.getMemberId(), inputMemberId);
    }
}
