package com.soothee.common.constants;

import lombok.Getter;

@Getter
public enum SnsType {
    KAKAOTALK("kakao"), GOOGLE("google");

    private final String snsType;

    SnsType(String snsType) {
        this.snsType = snsType;
    }
}
