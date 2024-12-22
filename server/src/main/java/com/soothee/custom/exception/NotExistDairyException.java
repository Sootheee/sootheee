package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import com.soothee.common.requestParam.MonthParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotExistDairyException extends ClassNotFoundException {
    public NotExistDairyException(Long memberId, MonthParam monthParam) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 년도 {" + monthParam.getYear() + "}, 월 {" + monthParam.getMonth() + "} 조건으로 조회된 일기가 없습니다.");
    }

    public NotExistDairyException(Long memberId, LocalDate date) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 작성 날짜 {" + date + "} 조건으로 조회된 일기가 없습니다.");
    }

    public NotExistDairyException(Long memberId, Long dairyId) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 " +
                "일기 일련번호 {" + dairyId + "} 조건으로 조회된 일기가 없습니다.");
    }

    public NotExistDairyException(Long dairyId) {
        super(DomainType.DAIRY.toKorean() + " 일련번호 {" + dairyId + "} 조건으로 조회된 일기가 없습니다.");
    }
}
