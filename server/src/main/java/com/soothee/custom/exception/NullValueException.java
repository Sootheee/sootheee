package com.soothee.custom.exception;

import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.DomainType;
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

    public NullValueException(Long condId, DomainType domainType) {
        super(domainType.toKorean() + " 일련번호{" + condId + "}로 조회된 " + domainType.toKorean() + "이 없습니다.");
    }
}
