package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BooleanYN {
    Y("Y"), N("N");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
