package com.soothee.member.domain;

import com.soothee.common.constants.DomainType;
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
    public MemberDelReason(Member member, DelReason delReason) throws IncorrectValueException, NullValueException {
        checkConstructorMemberDelReason(member, delReason);
        this.member = member;
        this.delReason = delReason;
    }

    @Override
    public Long getId() {
        return memberDelReasonId;
    }

    /** validation */
    private static void checkConstructorMemberDelReason(Member member, DelReason delReason) throws IncorrectValueException, NullValueException {
        SootheeValidation.checkDomain(member, DomainType.MEMBER);
        SootheeValidation.checkDomain(delReason, DomainType.DEL_REASON);
    }
}
