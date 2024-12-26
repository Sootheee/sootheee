package com.soothee.reference.domain;

import com.soothee.common.constants.ReferenceType;
import com.soothee.common.domain.Reference;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 탈퇴 사유 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "del_reason")
public class DelReason implements Reference {
    /** 탈퇴 사유 일련번호 */
    @Id
    private String reasonId;

    /** 탈퇴 사유 */
    @Column(name = "content", nullable = false)
    private String content;

    @Override
    public String getId() {
        return reasonId;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkReferenceId(getReasonId(), ReferenceType.DEL_REASON);
        SootheeValidation.checkNullForNecessaryString(getContent(), ReferenceType.DEL_REASON);
    }
}
