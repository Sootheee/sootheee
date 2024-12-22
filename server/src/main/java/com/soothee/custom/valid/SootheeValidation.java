package com.soothee.custom.valid;

import com.soothee.common.constants.*;
import com.soothee.common.domain.Domain;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

public class SootheeValidation {
    /**
     * Double 이 Null 아니고, 양수인지 검증
     *
     * @param value Double 검증할 Double
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkDouble(Double value, DoubleType type) throws NullValueException, IncorrectValueException {
        return checkIsNull(value, type) && checkCorrectDouble(value, type);
    }

    /**
     * date 가 Null 아니고, 2024년 이후 인지 검증
     *
     * @param date LocalDate 검증할 date
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkDate(LocalDate date) throws NullValueException, IncorrectValueException {
        return checkIsNull(date, StringType.DATE) && checkCorrectDate(date);
    }
    
    /**
     * Integer 가 Null 아니고, 양수인지 검증
     *
     * @param value Integer 검증할 Integer
     * @param type String Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkInteger(Integer value, CustomType type) throws NullValueException, IncorrectValueException {
        return checkIsNull(value, type) && checkCorrectInteger(value, type);
    }

    /**
     * 해당 도메인이 Null 아니고, 도메인의 id가 양수 인지 검증
     *
     * @param domain 검증할 Domain
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkDomain(Domain domain, DomainType type) throws NullValueException, IncorrectValueException {
        return checkIsNull(domain, type) && checkCorrectDomainId(domain, type);
    }

    /**
     * 해당 도메인 id 가 Null 아니고, 양수 인지 검증
     *
     * @param id 형식 검증할 Domain id
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkDomainId(Long id, DomainType type) throws NullValueException, IncorrectValueException {
        return checkIsNull(id, type) && checkCorrectDomainId(id, type);
    }

    /**
     * 해당 boolean 이 Null 아니고, "Y"나 "N" 인지 검증
     *
     * @param bool 검증할 boolean
     * @param type boolean msg
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkBoolean(String bool, BooleanType type) throws NullValueException, IncorrectValueException {
        return checkNullForNecessaryString(bool, type) && checkCorrectBoolean(bool, type);
    }

    /**
     * 해당 email 이 Null 아니고, 이메일 형식이 맞는지 검증
     *
     * @param email 검증할 email
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkEmail(String email) throws NullValueException, IncorrectValueException {
        return checkNullForNecessaryString(email, StringType.EMAIL) && checkCorrectEmail(email);
    }

    /**
     * 오늘의 요약의 길이가 600자 이하인지 검증
     * 필수 입력값이 아니기 때문에 Null 인 경우 Exception 발생하지 않고, false 만 리턴
     *
     * @param content 길이를 검증할 오늘의 요약
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkContent(String content) throws IncorrectValueException {
        if (checkNullForNotNecessaryString(content)) {
            return false;
        }
        return checkCorrectLength(content, StringType.CONTENT, 600, 0);
    }

    /**
     * 바랐던 방향성/감사한 일/배운 일의 길이가 200자 이하인지 검증
     * 필수 입력값이 아니기 때문에 Null 인 경우 Exception 발생하지 않고, false 만 리턴
     *
     * @param content 길이를 검증할 바랐던 방향성/감사한 일/배운 일
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkOptionalContent(String content, ContentType type) throws IncorrectValueException {
        if(checkNullForNotNecessaryString(content)) {
            return false;
        }
        return checkCorrectLength(content, type, 200, 0);
    }
    
    /**
     * 회원 닉네임이 Null 아니고, 길이가 6자 이하인지 검증
     *
     * @param name 길이를 검증할 회원 닉네임
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkName(String name) throws NullValueException, IncorrectValueException {
        return checkNullForNecessaryString(name, StringType.NAME) && checkCorrectLength(name, StringType.NAME, 6, 2);
    }

    /**
     * SNS 종류가 Null 아닌지 검증
     *
     * @param snsType Null 검증할 SnsType
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkSnsType(SnsType snsType) throws NullValueException {
        return checkIsNull(snsType, StringType.SNS_TYPE);
    }

    /**
     * 해당 값이 Null 아닌지 검증
     *
     * @param value Null 체크할 Object
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
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
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullForNecessaryString(String value, CustomType type) throws NullValueException {
        if (StringUtils.isBlank(value)) {
            throw new NullValueException(type);
        }
        return true;
    }

    /**
     * 해당 String 이 Null or "" or " " 아닌지 확인만 함
     * 필수 입력값이 아니기 때문에 Null 인 경우 Exception 발생하지 않고, false 만 리턴
     *
     * @param value Null 체크할 String
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkNullForNotNecessaryString(String value)   {
        return StringUtils.isBlank(value);
    }   

    /**
     * Double 가 양수인지 검증
     *
     * @param value 형식 검증할 Double
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
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
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectDate(LocalDate date) throws IncorrectValueException {
        if (date.getYear() < 2023 || date.getYear() > 2100) {
            throw new IncorrectValueException(date);
        }
        return true;
    }
    
    /**
     * Integer 이 양수인지 검증
     *
     * @param value 검증할 Integer
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectInteger(Integer value, CustomType type) throws IncorrectValueException {
        if (value < 0) {
            if (Objects.equals(type, StringType.ORDER_NO)) {
                throw new IncorrectValueException(value);
            }
            throw new IncorrectValueException(type, value);
        }
        return true;
    }

    /**
     * 해당 도메인의 id가 양수 인지 검증
     *
     * @param domain 형식 검증할 Domain
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectDomainId(Domain domain, DomainType type) throws IncorrectValueException {
        return checkCorrectDomainId(domain.getId(), type);
    }

    /**
     * 해당 도메인의 id가 양수 인지 검증
     *
     * @param id 형식 검증할 Domain id
     * @param type Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
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
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectBoolean(String bool, BooleanType type) throws IncorrectValueException {
        bool = StringUtils.trim(bool);
        if (!StringUtils.equals(bool, "Y") && !StringUtils.equals(bool, "N")) {
            throw new IncorrectValueException(type, bool);
        }
        return true;
    }

    /**
     * 해당 email 형식이 맞는지 검증
     *
     * @param email Null 체크할 email
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectEmail(String email) throws IncorrectValueException {
        email = StringUtils.trim(email);
        String regex = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (!Pattern.compile(regex).matcher(email).matches()) {
            throw new IncorrectValueException(email);
        }
        return true;
    }

    /**
     * 해당 String의 길이가 입력 가능 최대 길이 이하인지 검증
     *
     * @param value 길이 검증할 email
     * @param type Exception의 error message에 적용할 설명
     * @param max 입력 가능 최대 길이
     * @param min 입력 필수 최소 길이
     * @return 맞으면 true / 아니면 false
     */
    private static boolean checkCorrectLength(String value, CustomType type, int max, int min) throws IncorrectValueException {
        value = StringUtils.trim(value);
        if (value.length() > max && value.length() < min) {
            throw new IncorrectValueException(type, value, max, min);
        }
        return true;
    }
}
