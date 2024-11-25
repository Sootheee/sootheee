package com.soothee.common.constants;

public enum SnsType {
    KAKAOTALK("kakao"), GOOGLE("google");

    private final String snsType;

    SnsType(String snsType) {
        this.snsType = snsType;
    }

    public String toString(){
        return snsType;
    }
}
