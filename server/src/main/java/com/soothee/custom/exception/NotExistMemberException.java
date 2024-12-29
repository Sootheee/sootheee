package com.soothee.custom.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException(Long memberId) {
        super("해당 회원 일련번호 {" + memberId + "}로 조회된 회원이 없습니다.");
    }
}
