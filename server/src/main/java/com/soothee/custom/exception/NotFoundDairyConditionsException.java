package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotFoundDairyConditionsException extends RuntimeException {
    public NotFoundDairyConditionsException(Long dairyId) {
        super(DomainType.DAIRY.toKorean() + " 일련번호 {" + dairyId + "}에 선택된 " + ReferenceType.CONDITION + "이 존재하지만, 세부 정보 조회에 실패했습니다.");
    }
}
