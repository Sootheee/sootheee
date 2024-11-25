package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
import com.soothee.member.dto.UpdateMemberDTO;
import com.soothee.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    /** OAuth2 로그인 시, 받은 정보로 회원 조회</hr>
     *
     * @param oauth2ClientId String : OAuth2 로그인용 ID
     * @param snsType SnsType : OAuth2 로그인한 SNS 종류(GOOGLE or KAKAO)
     * @return Optional<Member> : 해당 정보의 맴버(Null 일 수도 있음)
     */
    public Optional<Member> getMemberForOAuth2(String oauth2ClientId, SnsType snsType) {
        return memberRepository.findByOauth2ClientIdAndSnsType(oauth2ClientId, snsType);
    }

    /** 회원 가입</hr>
     *
     * @param member Member : 가입할 회원의 정보
     */
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    /** 회원 닉네임 수정</hr>
     *
     * @param loginMember Member : 현재 로그인한 회원 정보
     * @param updateInfo UpdateMemberDTO: 수정할 회원의 정보
     */
    @Override
    public void updateMember(Member loginMember, UpdateMemberDTO updateInfo) {
        loginMember.updateMember(updateInfo.getMemberName());
    }

    /** 회원 탈퇴</hr>
     *
     * @param loginMember Member : 현재 로그인한 회원 정보
     */
    @Override
    public void deleteMember(Member loginMember) {
        loginMember.deleteMember();
    }

    /**
     * 현재 로그인한 회원 정보 가져오기</hr>
     *
     * @param principal Principal : 현재 로그인 계정 정보
     * @return Member: 로그인한 회원의 정보
     */
    @Override
    public Member getLoginMember(Principal principal) {
        String loginId = principal.getName();
        Optional<Member> optional =  memberRepository.findByEmail(loginId);
        return optional.orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, MyErrorMsg.NOT_EXIST_MEMBER));
    }

    /** 로그인한 회원의 정보와 입력된 회원의 정보가 일치하지 않는지 확인</hr>
     *
     * @param loginInfo Member : 로그인한 회원 정보
     * @param inputInfo UpdateMemberDTO : 입력한 회원 정보
     * @return 일치하면 false, 아니면 true
     */
    @Override
    public boolean isNotLoginMemberInfo(Member loginInfo, UpdateMemberDTO inputInfo) {
        return !Objects.equals(loginInfo.getId(), inputInfo.getId());
    }
}
