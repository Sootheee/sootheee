package com.soothee.custom.exception;

import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.ReferenceType;
import com.soothee.common.constants.StringType;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotMatchedException extends RuntimeException {
    public NotMatchedException(Dairy curDairy, DairyCondition curCondition) {
        super(DomainType.DAIRY.toKorean() + "의 일련번호 {" + curDairy.getDairyId() + "}와 "
                + DomainType.DAIRY_CONDITION.toKorean() + " 일련번호 {" + curCondition.getDairyCondId() + "}가 속한 "
                + DomainType.DAIRY.toKorean() + "의 일련번호 {" + curCondition.getDairy().getDairyId() + "}가 일치하지 않습니다.");
    }

    public NotMatchedException(Long curId, Long inputId, CustomType type) {
        super("기존 " + type.toKorean() + "의 일련번호 {" + curId + "}와 "
                + " 입력한 " + type.toKorean() + "의 일련번호 {" + inputId + "}가 일치하지 않습니다.");
    }

    public NotMatchedException(String curId, String inputId, ReferenceType type) {
        super("기존 " + type.toKorean() + "의 일련번호 {" + curId + "}와 "
                + " 입력한 " + type.toKorean() + "의 일련번호 {" + inputId + "}가 일치하지 않습니다.");
    }

    public NotMatchedException(LocalDate curDate, LocalDate inputDate) {
        super("기존 " + StringType.DATE.toKorean() + "{" + curDate + "}와 "
                + " 입력한 " + StringType.DATE.toKorean() + "{" + inputDate + "}가 일치하지 않습니다.");
    }
}
