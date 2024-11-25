package com.soothee.member.service;

import com.soothee.common.constants.SnsType;
import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.member.domain.Member;
import com.soothee.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** 로그인한 아이디(이메일)로 회원 정보 조회</hr>
     *
     * @param loginId String : 로그인한 회원 이메일
     * @return Member : 로그인한 회원의 정보
     */
    @Override
    public Member getMemberByEmail(String loginId) {
        Optional<Member> optional =  memberRepository.findByEmail(loginId);
        return optional.orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, MyErrorMsg.NOT_EXIST_MEMBER));
    }
}
