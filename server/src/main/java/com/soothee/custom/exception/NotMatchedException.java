package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.StringType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotMatchedException extends RuntimeException {
    public NotMatchedException(Long curDairyId, Long inputDairyId) {
        super("기존 " + DomainType.DAIRY.toKorean() + "의 일련번호 {" + curDairyId + "}와 "
                + " 입력한 " + DomainType.DAIRY.toKorean() + "의 일련번호 {" + inputDairyId + "}가 일치하지 않습니다.");
    }

    public NotMatchedException(LocalDate curDate, LocalDate inputDate) {
        super("기존 " + StringType.DATE.toKorean() + "{" + curDate + "}와 "
                + " 입력한 " + StringType.DATE.toKorean() + "{" + inputDate + "}가 일치하지 않습니다.");
    }
}
