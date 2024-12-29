package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException(Long memberId) {
        super("해당 " + DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}로 조회된 " + DomainType.MEMBER.toKorean() + "이 없습니다.");
    }
}
