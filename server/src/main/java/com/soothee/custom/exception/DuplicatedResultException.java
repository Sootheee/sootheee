package com.soothee.custom.exception;

import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuplicatedResultException extends RuntimeException {
    public DuplicatedResultException(DomainType type) {
        super(type.toKorean()+ "가 중복 조회되었습니다.");
    }

    public DuplicatedResultException(int count, CustomType type) {
        super(type + "관련 통계 결과가 {" + count + "}개로 중복 오류가 발생했습니다.");
    }

    public DuplicatedResultException(int count, CustomType type, SortType sortType) {
        super(type + " " + sortType + "관련 통계 결과가 {" + count + "}개로 중복 오류가 발생했습니다.");
    }
}
