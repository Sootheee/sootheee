package com.soothee.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MyErrorMsg {
    NOT_EXIST_MEMBER("해당 정보로 조회된 회원이 없습니다."),
    NULL_VALUE("값이 없습니다."),
    MISS_MATCH_MEMBER("로그인한 회원의 정보가 아닙니다.");

    private final String msg;

    public String toString() {
        return msg;
    }
}
