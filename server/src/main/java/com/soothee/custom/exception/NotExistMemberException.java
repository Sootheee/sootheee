package com.soothee.custom.exception;

import com.soothee.oauth2.domain.AuthenticatedUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException(AuthenticatedUser loginInfo) {
        super("로그인한 계정 {" + loginInfo.getEmail() + "}의 회원 정보가 없습니다.");
    }

    public NotExistMemberException(Long memberId) {
        super("해당 회원 일련번호 {" + memberId + "}로 조회된 회원이 없습니다.");
    }
}
