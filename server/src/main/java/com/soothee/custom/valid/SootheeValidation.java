package com.soothee.custom.valid;

import com.soothee.common.constants.*;
import com.soothee.custom.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

public class SootheeValidation {
    /**
     * type query parameter 값이 name 인지 확인 검증
     *
     * @param type 검증할 type query parameter
     */
    public static void validTypeQueryParameter(String type) throws IncorrectParameterException {
        if (isNotNullNotNecessaryString(type) && !StringUtils.equals(type, "name")) {
            throw new IncorrectParameterException(type, true);
        }
    }

    /**
     * Path Parameter 가 올바른지 검증
     *
     * @param year 조회할 년도
     * @param month 조회할 월
     */
    public static void validYearMonthPathParam(int year, int month) throws IncorrectParameterException {
        if (year < 2024 || year > 2100 || month < 1 || month > 12) {
            throw new IncorrectParameterException(year, month, true);
        }
    }

    /**
     * Path Parameter 가 올바른지 검증
     *
     * @param year 조회할 년도
     * @param week 조회할 주차
     */
    public static void validYearWeekPathParam(int year, int week) throws IncorrectParameterException {
        if (year < 2020 || year > 2100 || week < 1 || week > 53) {
            throw new IncorrectParameterException(year, week, false);
        }
    }

    /**
     * Path Parameter 가 올바른지 검증
     *
     * @param date 조회할 날짜
     */
    public static void validDatePathParam(LocalDate date) throws IncorrectParameterException {
        if (date.isBefore(LocalDate.of(2024,1,1))
                || date.isAfter(LocalDate.of(2100,12,31))) {
            throw new IncorrectParameterException(date);
        }
    }

    /**
     * Path Parameter 가 올바른지 검증
     *
     * @param dairyId 조회할 일기 일련번호
     */
    public static void validDairyIdPathParam(Long dairyId) throws IncorrectParameterException {
        if (dairyId < 1) {
            throw new IncorrectParameterException(dairyId, true);
        }
    }

    /**
     * Path Parameter 가 올바른지 검증
     *
     * @param memberId 조회할 일기 일련번호
     */
    public static void validMemberIdPathParam(Long memberId) throws IncorrectParameterException {
        if (memberId < 1) {
            throw new IncorrectParameterException(memberId, false);
        }
    }

    /**
     * 회원 닉네임 수정 요청 파라미터인 name 이 null 인지 검증
     *
     * @param name Null 체크할 name 값
     */
    public static void validNameQueryParameter(String name) throws IncorrectParameterException {
        if (StringUtils.isBlank(name)) {
            throw new IncorrectParameterException(name, false);
        }
        name = StringUtils.trim(name);
        if (name.length() < 2 || name.length() > 6) {
            throw new IncorrectParameterException(name, false);
        }
    }

    /**
     * 통계 중복 (결과 갯수 2개 이상) 검증
     *
     * @param count 통계 결과 갯수
     */
    public static void checkOnlyOneStats(int count, SortType sortType) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException(count, sortType);
        }
    }

    /**
     * 통계 중복 (결과 갯수 2개 이상) 검증
     *
     * @param count 통계 결과 갯수
     */
    public static void checkOnlyOneStats(int count) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException(count);
        }
    }

    /**
     * 일기 중복 조회 검증
     *
     * @param count 검증할 조회된 일기 갯수
     */
    public static void checkOnlyOneResult(int count) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException();
        }
    }

    /**
     * 기존 일련번호와 입력된 일련번호가 일치하지 않는지 검증
     *
     * @param curId 기존 일기 일련번호
     * @param inputId 수정할 일기 일련번호
     */
    public static void checkSameDairy(Long curId, Long inputId) throws NotMatchedException {
        if (!Objects.equals(curId, inputId)) {
            throw new NotMatchedException(curId, inputId);
        }
    }

    /**
     * 현재 로그인한 회원이 해당 일기 작성자인지 검증
     *
     * @param curId 로그인한 회원 일련번호
     * @param inputId 일기 작성 회원 일련번호
     */
    public static void checkAuthorizedMember(Long curId, Long inputId) throws NoAuthorizeException {
        if (!Objects.equals(curId, inputId)) {
            throw new NoAuthorizeException(curId, inputId);
        }
    }

    /**
     * 기존 작성 날짜와 입력된 작성 날짜가 일치하지 않는지 검증
     *
     * @param curDate 기존 작성 날짜
     * @param inputDate 입력한 작성 날짜
     */
    public static void checkSameDate(LocalDate curDate, LocalDate inputDate) throws NotMatchedException {
        if (!Objects.equals(curDate, inputDate)) {
            throw new NotMatchedException(curDate, inputDate);
        }
    }

    /**
     * 조회한 갯수와 실제 세부 리스트의 아이템 갯수가 일치하는지 검증
     *
     * @param count 조회해서 나온 갯수
     * @param size 세부 리스트의 아이템 갯수
     */
    public static void checkSameResultCount(Integer count, int size, CustomType type) throws ErrorToSearchStatsException {
        if (count != size) {
            throw new ErrorToSearchStatsException(count, size, type);
        }
    }

    /**
     * 해당 String 이 Null or "" or " " 아닌지 확인만 함
     * 필수 입력값이 아니기 때문에 Null 인 경우 Exception 발생하지 않고, false 만 리턴
     *
     * @param value Null 체크할 String
     * @return null 아니면 true / 맞으면 false
     */
    private static boolean isNotNullNotNecessaryString(String value)   {
        return StringUtils.isNotBlank(value);
    }
}
