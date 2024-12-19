package com.soothee.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MyErrorMsg {
    NOT_EXIST_MEMBER("해당 정보로 조회된 회원이 없습니다."),
    NOT_EXIST_DAIRY("해당 조건으로 조회된 일기가 없습니다."),
    NULL_VALUE("값이 없습니다."),
    MISS_MATCH_MEMBER("로그인한 회원의 정보가 아닙니다."),
    DUPLICATED_DAIRY("해당 일자에 생성된 일기가 1 개를 초과했습니다."),
    ALREADY_EXIST_DAIRY("해당 일자에 이미 생성된 일기가 있습니다."),
    NOT_ENOUGH_DAIRY_COUNT("작성한 일기가 통계 가능한 최소 일기 수(3 개)보다 부족합니다."),
    NO_CONTENTS("해당 옵션의 기록이 없습니다.");

    private final String msg;

    public String toString() {
        return msg;
    }

    public String makeValue(String msg){
        return msg + NULL_VALUE.msg;
    }
}
