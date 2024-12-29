package com.soothee.custom.valid;

import com.soothee.common.constants.*;
import com.soothee.common.domain.Domain;
import com.soothee.common.domain.Reference;
import com.soothee.custom.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

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
     * 통계 중복 (결과 갯수 1개 초과) 검증
     *
     * @param count 통계 결과 갯수
     */
    public static void checkStatsDuplicate(int count, CustomType type,  SortType sortType) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException(count, type, sortType);
        }
    }

    /**
     * 통계 중복 (결과 갯수 1개 초과) 검증
     *
     * @param count 통계 결과 갯수
     */
    public static void checkStatsDuplicate(int count, CustomType type) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException(count, type);
        }
    }

    /**
     * 일기 중복 조회 검증
     *
     * @param count 검증할 조회된 일기 갯수
     */
    public static void checkDairyDuplicate(int count) throws DuplicatedResultException {
        if (count > 1) {
            throw new DuplicatedResultException(DomainType.DAIRY);
        }
    }

    /**
     * 기존 일련번호와 입력된 일련번호가 일치하지 않는지 검증
     *
     * @param curId 기존 일련번호
     * @param inputId 입력한 일련번호
     */
    public static void checkMatchedId(Long curId, Long inputId, CustomType type) throws NotMatchedException {
        /* 로그인한 계정의 회원 일련번호와 입력된 회원 일련번호가 일치하는지 확인 */
        if (!Objects.equals(curId, inputId)) {
            /* 로그인한 계정의 회원 일련번호와 입력한 회원 일련번호가 일치하지 않는 경우 Exception 발생 */
            throw new NotMatchedException(curId, inputId, type);
        }
    }

    /**
     * 기존 작성 날짜와 입력된 작성 날짜가 일치하지 않는지 검증
     *
     * @param curDate 기존 작성 날짜
     * @param inputDate 입력한 작성 날짜
     */
    public static void checkMatchedDate(LocalDate curDate, LocalDate inputDate) throws NotMatchedException {
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
    public static void checkListSize(Integer count, int size, CustomType type) throws ErrorToSearchStatsException {
        if (count != size) {
            throw new ErrorToSearchStatsException(count, size, type);
        }
    }

    /**
     * 조회한 Role 이 맞는지 검증
     *
     * @param role 검증할 role
     */
    public static void checkRole(Role role) throws NullValueException {
        checkIsNull(role, StringType.ROLE);
    }

    /**
     * 해당 값이 Null 아닌지 검증
     *
     * @param value Null 체크할 Object
     * @param type Exception error message 적용할 설명
     * @return 맞으면 true / 아니면 Exception
     */
    private static boolean checkIsNull(Object value, CustomType type) throws NullValueException {
        if (Objects.isNull(value)) {
            throw new NullValueException(type);
        }
        return true;
    }

    /**
     * 해당 String 이 Null 아닌지 검증
     *
     * @param value Null 체크할 String
     * @param type Exception error message 적용할 설명
     */
    public static void checkNullForNecessaryString(String value, CustomType type) throws NullValueException {
        if (StringUtils.isBlank(value)) {
            throw new NullValueException(type);
        }
    }

    /**
     * 해당 String 이 Null or "" or " " 아닌지 확인만 함
     * 필수 입력값이 아니기 때문에 Null 인 경우 Exception 발생하지 않고, false 만 리턴
     *
     * @param value Null 체크할 String
     */
    private static boolean checkNullForNotNecessaryString(String value)   {
        return StringUtils.isBlank(value);
    }   

    /**
     * Double 가 양수인지 검증
     *
     * @param value 형식 검증할 Double
     * @param type Exception error message 적용할 설명
     * @return 맞으면 true / 아니면 Exception
     */
    private static boolean checkCorrectDouble(Double value, DoubleType type) throws IncorrectValueException {
        if (value < 0) {
            throw new IncorrectValueException(type, value);
        }
        return true;
    }

    /**
     * date 가 2024년 이후이고 2100년 이전인지 검증
     *
     * @param date 형식 검증할 date
     */
    private static void checkCorrectDate(LocalDate date) throws IncorrectValueException {
        if (date.getYear() < 2023 || date.getYear() > 2100) {
            throw new IncorrectValueException(date);
        }
    }
    
    /**
     * Integer 이 양수인지 검증
     *
     * @param value 검증할 Integer
     * @param type Exception error message 적용할 설명
     */
    private static void checkCorrectInteger(Integer value, CustomType type) throws IncorrectValueException {
        if (value < 0) {
            if (Objects.equals(type, StringType.ORDER_NO)) {
                throw new IncorrectValueException(value);
            }
            throw new IncorrectValueException(type, value);
        }
    }

    /**
     * Reference 가 존재하는 Reference 인지 검증
     *
     * @param reference 검증할 Reference
     * @param type Exception error message 적용할 설명
     */
    private static void checkCorrectReference(Reference reference, ReferenceType type) throws IncorrectValueException {
        checkCorrectReferenceId(reference.getId(), type);
    }

    /**
     * Reference id가 존재하는 일련번호인지 검증
     *
     * @param id 검증할 id
     * @param type Exception error message 적용할 설명
     */
    private static void checkCorrectReferenceId(String id, ReferenceType type) throws IncorrectValueException {
        int idInt = Integer.parseInt(StringUtils.substring(id, 4));
        int max = 100;
        if (Objects.equals(type, ReferenceType.CONDITION_TYPE)) {
            max = 3;
        }
        if (Objects.equals(type, ReferenceType.CONDITION)) {
            max = 15;
        }
        if (Objects.equals(type, ReferenceType.DEL_REASON)) {
            max = 5;
        }
        if (Objects.equals(type, ReferenceType.WEATHER)) {
            max = 6;
        }
        if (idInt < 1 || idInt > max) {
            throw new IncorrectValueException(type, id, 1, max);
        }
    }

    /**
     * 해당 도메인의 id가 양수 인지 검증
     *
     * @param domain 형식 검증할 Domain
     * @param type Exception error message 적용할 설명
     */
    private static void checkCorrectDomainId(Domain domain, DomainType type) throws IncorrectValueException {
        checkCorrectDomainId(domain.getId(), type);
    }

    /**
     * 해당 도메인의 id가 양수 인지 검증
     *
     * @param id 형식 검증할 Domain id
     * @param type Exception error message 적용할 설명
     * @return 맞으면 true / 아니면 Exception
     */
    private static boolean checkCorrectDomainId(Long id, DomainType type) throws IncorrectValueException {
        if (id < 1) {
            throw new IncorrectValueException(type, id);
        }
        return true;
    }

    /**
     * 해당 boolean 이 "Y"나 "N" 인지 검증
     *
     * @param bool 형식 검증할 boolean
     * @param type boolean msg
     */
    private static void checkCorrectBoolean(String bool, BooleanType type) throws IncorrectValueException {
        bool = StringUtils.trim(bool);
        if (!StringUtils.equals(bool, BooleanYN.Y.toString()) && !StringUtils.equals(bool, BooleanYN.N.toString())) {
            throw new IncorrectValueException(type, bool);
        }
    }

    /**
     * 해당 email 형식이 맞는지 검증
     *
     * @param email Null 체크할 email
     */
    private static void checkCorrectEmail(String email) throws IncorrectValueException {
        email = StringUtils.trim(email);
        String regex = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!Pattern.compile(regex).matcher(email).matches()) {
            throw new IncorrectValueException(email);
        }
    }

    /**
     * 해당 String 의 길이가 입력 가능 최대 길이 이하인지 검증
     *
     * @param value 길이 검증할 email
     * @param type Exception error message 적용할 설명
     * @param max 입력 가능 최대 길이
     * @param min 입력 필수 최소 길이
     */
    private static void checkCorrectLength(String value, CustomType type, int min, int max) throws IncorrectValueException {
        value = StringUtils.trim(value);
        if (value.length() > max && value.length() < min) {
            throw new IncorrectValueException(type, value, min, max);
        }
    }
}
