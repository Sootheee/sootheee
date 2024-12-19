package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentType {
    THANKS("thanks"), LEARN("learn");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
