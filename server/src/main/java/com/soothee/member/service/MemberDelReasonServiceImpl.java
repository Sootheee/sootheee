package com.soothee.member.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
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
    public void saveDeleteReasons(Member loginMember, List<String> delReasonIdList) throws NullValueException, IncorrectValueException {
        for (String delReasonId : delReasonIdList) {
            SootheeValidation.checkReferenceId(delReasonId, ReferenceType.DEL_REASON);
            /* 입력된 탈퇴 사유 일련번호로 탈퇴 사유 조회
             * - 탈퇴 사유 일련번호로 조회된 탈퇴 사유가 없는 경우 Exception 발생 */
            DelReason delReason = delReasonService.getDelReasonById(delReasonId);
            /* 회원 탈퇴 사유 등록
             * - 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생 */
            memberDelReasonRepository.save(MemberDelReason.builder()
                                                            .member(loginMember)
                                                            .delReason(delReason)
                                                            .build());
        }
    }

    @Override
    public List<MemberDelReason> getMemberDelReasonByMemberId(Long memberId) throws NullValueException, IncorrectValueException {
        /* 회원 일련번호로 회원 탈퇴 사유 조회 */
        List<MemberDelReason> result = memberDelReasonRepository.findByMemberMemberIdAndMemberIsDelete(memberId, BooleanYN.N.toString())
                /* 회원 일련번호로 조회된 회원 탈퇴 사유가 없는 경우 Exception 발생 */
                .orElseThrow(() -> new NullValueException(memberId, DomainType.MEMBER_DEL_REASON));
        for (MemberDelReason memberDelReason : result) {
            memberDelReason.valid();
        }
        return result;
    }
}
