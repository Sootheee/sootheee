package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.SortType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotFoundDetailInfoException extends RuntimeException{
    public NotFoundDetailInfoException(CustomType type) {
        super(type.toKorean() + "이 존재하지만 " + type.toKorean() + "의 정보 조회에 실패했습니다.");

    }
    public NotFoundDetailInfoException(ContentType type, SortType sortType) {
        super(type.toKorean() + "이 존재하지만 가장 " + sortType.toKorean() + "의 " + type.toKorean() + " 정보 조회에 실패했습니다.");
    }
}
