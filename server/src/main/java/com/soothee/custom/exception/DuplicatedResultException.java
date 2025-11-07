package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;

public class DuplicatedResultException extends RuntimeException {
    public DuplicatedResultException() {
        super(DomainType.DAIRY.toKorean()+ "가 중복 조회되었습니다.");
    }

    public DuplicatedResultException(int count) {
        super("통계 결과가 {" + count + "}개로 중복 조회되었습니다.");
    }

    public DuplicatedResultException(int count, SortType sortType) {
        super(sortType + " 통계 결과가 {" + count + "}개로 중복 조회되었습니다.");
    }
}
