package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.DomainType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuplicatedResultException extends ClassNotFoundException {
    public DuplicatedResultException(Long memberId, MonthParam monthParam) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear() + "}, " +
                "월 {" + monthParam.getMonth() + "} 조건의 " + DomainType.DAIRY.toKorean() + " 월간 통계 요약 정보가 1개 이상 입니다.");
    }

    public DuplicatedResultException(Long memberId, LocalDate date) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 작성 날짜 {" + date + "} " +
                "조건으로 조회된 " + DomainType.DAIRY.toKorean() + " 정보가 1개 이상 입니다.");
    }

    public DuplicatedResultException(Long memberId, Long dairyId) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}와 " + DomainType.DAIRY.toKorean() + " 일련번호 {"
                + dairyId + "} " +"조건으로 조회된 " + DomainType.DAIRY.toKorean() + " 정보가 1개 이상 입니다.");
    }

    public DuplicatedResultException(Long memberId, ContentType type, MonthParam monthParam) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear() + "}," +
                " 월 {" + monthParam.getMonth() + "} 조건으로 조회된 " + type.toKorean() + " 갯수 결과 값이 1개 이상 입니다.");
    }

    public DuplicatedResultException(Long memberId, ContentType type, MonthParam monthParam, SortType sortType) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear() + "}, " +
                "월{" + monthParam.getMonth() + "} 조건으로 조회된 가장 " + sortType.toKorean() + "의 " + type.toKorean() + "이 1개 이상 입니다.");
    }

    public DuplicatedResultException(Long memberId, WeekParam weekParam) {
        super(DomainType.MEMBER.toKorean() + " 일련번호 {" + memberId + "}, 년도 {" + weekParam.getYear() + "}, " +
                "월 {" + weekParam.getWeek() + "} 조건의 " + DomainType.DAIRY.toKorean() + " 주간 요약 정보가 1개 이상 입니다.");
    }
}
