package com.soothee.custom.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotFoundDairyConditionsException extends RuntimeException {
    public NotFoundDairyConditionsException(Long dairyId) {
        super("일기 일련번호 {" + dairyId + "}에 선택된 컨디션이 존재하지만, 세부 정보 조회에 실패했습니다.");
    }
}
