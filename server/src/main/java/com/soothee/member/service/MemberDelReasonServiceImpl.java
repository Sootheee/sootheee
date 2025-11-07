package com.soothee.member.service;

import com.soothee.common.constants.DomainType;
import com.soothee.custom.exception.NullValueException;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.member.repository.MemberDelReasonRepository;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.service.DelReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberDelReasonServiceImpl implements MemberDelReasonService {
    private final MemberDelReasonRepository memberDelReasonRepository;
    private final DelReasonService delReasonService;

    @Override
    public void saveDeleteReasons(Member loginMember, List<String> delReasonIdList) throws NullValueException {
        for (String delReasonId : delReasonIdList) {
            DelReason delReason = delReasonService.getDelReasonById(delReasonId);
            memberDelReasonRepository.save(MemberDelReason.builder()
                                                            .member(loginMember)
                                                            .delReason(delReason)
                                                            .build());
        }
    }

    @Override
    public List<MemberDelReason> getMemberDelReasonByMemberId(Long memberId) throws NullValueException {
        return memberDelReasonRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new NullValueException(memberId, DomainType.MEMBER_DEL_REASON));
    }
}
