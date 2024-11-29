package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
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

    /**
     * OAuth2 로그인 시, 받은 정보로 회원 조회</hr>
     *
     * @param oauth2ClientId String : OAuth2 로그인용 ID
     * @param snsType SnsType : OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
     * @return Optional<Member> : 해당 정보의 맴버(Null 일 수도 있음)
     */
    @Override
    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        return memberRepository.findByOauth2ClientIdAndSnsType(oauth2ClientId, snsType);
    }

    /**
     * 회원 가입</hr>
     *
     * @param member Member : 가입할 회원의 정보
     */
    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    /**
     * 회원 닉네임 수정</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @param memberId Long : 입력한 회원 일련번호
     * @param updateName String : 변경할 닉네임
     */
    @Override
    public void updateName(AuthenticatedUser loginInfo, Long memberId, String updateName) {
        Member loginMember = this.getLoginMember(loginInfo);
        if(this.isNotLoginMemberInfo(loginMember, memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        loginMember.updateName(updateName);
    }

    /**
     * 회원 다크모드 수정</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @param memberId Long : 입력한 회원 일련번호
     * @param updateMode String : 변경할 모드
     */
    @Override
    public void updateDarkMode(AuthenticatedUser loginInfo, Long memberId, String updateMode) {
        Member loginMember = this.getLoginMember(loginInfo);
        if(this.isNotLoginMemberInfo(loginMember, memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        loginMember.updateDarkModeYN(updateMode);
    }

    /**
     * 회원 탈퇴</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인 계정 정보
     * @param memberId Long : 입력한 회원 일련번호
     */
    @Override
    public void deleteMember(AuthenticatedUser loginInfo, Long memberId) {
        Member loginMember =  this.getLoginMember(loginInfo);
        if(this.isNotLoginMemberInfo(loginMember, memberId)) {
            throw new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.MISS_MATCH_MEMBER);
        }
        loginMember.deleteMember();
    }

    /**
     * 로그인한 회원의 모든 정보 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @return MemberInfoDTO : 회원의 모든 정보
     */
    @Override
    public MemberInfoDTO getAllMemberInfo(AuthenticatedUser loginInfo) {
        Member loginMember = this.getLoginMember(loginInfo);
        return MemberInfoDTO.fromMember(loginMember);
    }

    /**
     * 로그인한 회원의 닉네임만 조회</hr>
     *
     * @param loginInfo AuthenticatedUser : 로그인한 회원 정보
     * @return MemberNameDTO : 회원 일련번호와 닉네임 정보
     */
    @Override
    public MemberNameDTO getNicknameInfo(AuthenticatedUser loginInfo) {
        Member loginMember = this.getLoginMember(loginInfo);
        return MemberNameDTO.fromMember(loginMember);
    }

    /**
     * 현재 로그인한 회원 정보 가져오기</hr>
     *
     * @param loginInfo AuthenticatedUser : 현재 로그인 계정 정보
     * @return Member: 로그인한 회원의 정보
     */
    @Override
    public Member getLoginMember(AuthenticatedUser loginInfo) {
        String oauth2Id = loginInfo.getName();
        Optional<Member> optional =  memberRepository.findByOauth2ClientId(oauth2Id);
        return optional.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, MyErrorMsg.NOT_EXIST_MEMBER));
    }

    /**
     * 로그인한 회원의 정보와 입력된 회원의 정보가 일치하지 않는지 확인</hr>
     *
     * @param loginInfo Member : 로그인한 회원 정보
     * @param inputMemberId Long : 입력한 회원 일련번호
     * @return 일치하면 false, 아니면 true
     */
    private boolean isNotLoginMemberInfo(Member loginInfo, Long inputMemberId) {
        return !Objects.equals(loginInfo.getMemberId(), inputMemberId);
    }
}
