package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoAuthorizeException extends RuntimeException {
    public NoAuthorizeException(Long loginMemberId, Long inputMemberId) {
        super("현재 로그인한 " + DomainType.MEMBER.toKorean() + "의 일련번호{" + loginMemberId + "}와 " +
                "해당 " + DomainType.DAIRY.toKorean() + "를 작성한 " + DomainType.MEMBER.toKorean() + "의 일련번호{" + inputMemberId + "}가 일치하지 않아 권한이 없습니다.");
    }
}
