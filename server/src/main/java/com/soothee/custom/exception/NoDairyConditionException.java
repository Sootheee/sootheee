package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoDairyConditionException extends ClassNotFoundException {
    public NoDairyConditionException(Long dairyId) {
        super(DomainType.DAIRY.toKorean() + " 일련번호 {" + dairyId + "}의 "
                + DomainType.DAIRY_CONDITION.toKorean() + "가 존재하지만 정보가 없습니다.");
    }
}
