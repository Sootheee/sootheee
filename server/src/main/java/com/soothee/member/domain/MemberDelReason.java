package com.soothee.member.domain;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import com.soothee.common.domain.Domain;
import com.soothee.common.domain.TimeEntity;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import com.soothee.reference.domain.DelReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_del_reason")
public class MemberDelReason extends TimeEntity implements Domain {
    /** 회원 탈퇴 사유 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberDelReasonId;

    /** 회원 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /** 탈퇴 사유 일련번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_id")
    private DelReason delReason;

    @Builder
    public MemberDelReason(Member member, DelReason delReason) {
        this.member = member;
        this.delReason = delReason;
    }

    @Override
    public Long getId() {
        return memberDelReasonId;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomainId(getMemberDelReasonId(), DomainType.MEMBER_DEL_REASON);
        SootheeValidation.checkDomain(getMember(), DomainType.MEMBER);
        SootheeValidation.checkReference(getDelReason(), ReferenceType.DEL_REASON);
    }
}
