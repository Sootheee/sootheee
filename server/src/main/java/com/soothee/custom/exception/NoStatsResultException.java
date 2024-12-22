package com.soothee.custom.exception;

import com.soothee.common.constants.DomainType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoStatsResultException extends ClassNotFoundException {
    public NoStatsResultException(Long memberId, MonthParam monthParam) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear()
                + "}, 월{" + monthParam.getMonth() + "} 조건의 선택된 "
                + DomainType.CONDITION.toKorean() + "이 없습니다.");
    }

    public NoStatsResultException(Long memberId, MonthParam monthParam, Integer count) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + monthParam.getYear()
                + "}, 월{" + monthParam.getMonth() + "} 조건의 " +
                "선택된 " + DomainType.CONDITION.toKorean() + "이 "
                + count + "개 있지만, 조회된 " + DomainType.CONDITION.toKorean() + "의 정보가 없습니다.");
    }

    public NoStatsResultException(Long memberId, WeekParam weekParam) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + weekParam.getYear()
                + "}, 주차{" + weekParam.getWeek() + "} 조건으로 조회된 " + DomainType.DAIRY.toKorean() + "가 없습니다.");
    }

    public NoStatsResultException(Long memberId, WeekParam weekParam, Integer count) {
        super(DomainType.MEMBER.toKorean() + "일련번호 {" + memberId + "}, 년도 {" + weekParam.getYear()
                + "}, 주차{" + weekParam.getWeek() + "} 조건으로 조회된 " + DomainType.DAIRY.toKorean() + "가 "
                + count + "개 있지만, 조회된 " + DomainType.DAIRY.toKorean() + "의 정보가 없습니다.");
    }
}
