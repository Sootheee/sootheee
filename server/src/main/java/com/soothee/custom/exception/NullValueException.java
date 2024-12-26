package com.soothee.custom.exception;

import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NullValueException extends RuntimeException {
    public NullValueException(CustomType type) {
        super(type.toKorean() + "이 Null 입니다.");
    }

    public NullValueException(String type) {
        super(type + "이 Null 입니다.");
    }

    public NullValueException(Long memberId, DomainType domainType) {
        super(DomainType.MEMBER.toKorean() + " 일련번호{" + memberId + "}로 조회된 " + domainType.toKorean() + "이 없습니다.");
    }

    public NullValueException(String referenceId, ReferenceType referenceType) {
        super(referenceType.toKorean() + " 일련번호{" + referenceId + "}로 조회된 " + referenceType.toKorean() + "이 없습니다.");
    }

    public NullValueException(DomainType domainType) {
        super("입력된 " + domainType.toKorean() + "일련번호가 가 하나도 없습니다.");
    }
}
