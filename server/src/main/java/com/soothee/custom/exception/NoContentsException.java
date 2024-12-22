package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoContentsException extends ClassNotFoundException {
    public NoContentsException(Long memberId, ContentType type, MonthParam monthParam) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear() + "}, " +
                "월{" + monthParam.getMonth() + "} 조건으로 조회된 " + type.toKorean() + "이 없습니다.");
    }

    public NoContentsException(Long memberId, ContentType type, MonthParam monthParam, SortType sortType) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear() + "}, " +
                "월{" + monthParam.getMonth() + "} 조건으로 조회된 가장 " + sortType.toKorean() + "의 " + type.toKorean() + "이 없습니다.");
    }
}
