package com.soothee.common.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortType {
    DATE("date"), HIGH("high"), LOW("low");

    private final String sort;

    @Override
    public String toString() {
        return sort;
    }
}
