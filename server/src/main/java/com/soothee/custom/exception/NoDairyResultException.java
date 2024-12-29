package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoDairyResultException extends RuntimeException {
    public NoDairyResultException(Long memberId, LocalDate date) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와" +
                " 작성 날짜 {" + date + "} 조건으로 조회된 " + DomainType.DAIRY.toKorean() + "가 없습니다.");
    }

    public NoDairyResultException(Long memberId, Long dairyId) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 " +
                "일기 일련번호 {" + dairyId + "} 조건으로 조회된 일기가 없습니다.");
    }

    public NoDairyResultException(Long dairyId) {
        super(DomainType.DAIRY.toKorean() + " 일련번호 " +
                "{" + dairyId + "} 조건으로 조회된 " + DomainType.DAIRY.toKorean() + "가 없습니다.");
    }
}
