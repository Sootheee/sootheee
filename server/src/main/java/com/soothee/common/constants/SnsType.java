package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SnsType {
    KAKAOTALK("kakao"), GOOGLE("google");

    private final String snsType;

    public String toString(){
        return snsType;
    }
}
