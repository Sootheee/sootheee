package com.soothee.custom.exception;

import com.soothee.common.constants.CustomType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorToSearchStatsException extends RuntimeException {
    public ErrorToSearchStatsException(CustomType type) {
        super(type.toKorean() + "의 통계 결과가 없습니다.");
    }

    public ErrorToSearchStatsException(int count, int listSize, CustomType type) {
        super(type.toKorean() + "를 조회한 갯수가 {" + count + "}인데 " +
                "실세 세부 정보 리스트의 길이가 {" + listSize + "}로 세부 정보 조회에 실패했습니다.");
    }
}
