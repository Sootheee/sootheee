package com.soothee.common.exception;

import com.soothee.common.domain.Domain;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import com.soothee.member.domain.Member;
import com.soothee.member.domain.MemberDelReason;
import com.soothee.reference.domain.Condition;
import com.soothee.reference.domain.ConditionType;
import com.soothee.reference.domain.DelReason;
import com.soothee.reference.domain.Weather;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ErrorHandlingUtil {
    /**
     * 입력받은 각 파라미터의 validate 중 실패한 결과 response</hr>
     *
     * @param bindingResult BingingResult : validate 실패 결과
     * @return List<BindingErrorResult> : 결과 재구조화 리스트
     */
    public List<BindingErrorResult> getErrorResponse(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<BindingErrorResult> result = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            result.add(new BindingErrorResult(fieldError));
        }
        return result;
    }

    /**
     * score 가 Null 아니고, 양수인지 확인</hr>
     *
     * @param score Double : 확인할 score
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectScore(Double score) throws SootheeException {
        String msg = "점수 ";
        return checkNullScore(score, msg) && checkCorrectDouble(score, msg);
    }

    /**
     * date 가 Null 아니고, 2024년 이후 인지 확인</hr>
     *
     * @param date LocalDate : 확인할 date
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectDate(LocalDate date) throws SootheeException {
        String msg = "작성 날짜 ";
        return checkNullDate(date, msg) && checkCorrectLocalDate(date, msg);
    }

    /**
     * 해당 도메인이 Null 아니고, 도메인의 id가 양수 인지 확인</hr>
     *
     * @param domain Domain : 확인할 Domain
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectDomain(Domain domain) throws SootheeException {
        return checkNullDomain(domain) && checkCorrectDomainId(domain);
    }

    /**
     * 해당 boolean이 Null이 아니고, "Y"나 "N" 인지 확인</hr>
     *
     * @param bool String : 확인할 boolean
     * @param type String : boolean type
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkBoolean(String bool, String type) throws SootheeException {
        type += " ";
        if (StringUtils.isNotBlank(bool)) {
            throw new SootheeException(MyErrorMsg.NULL_VALUE.makeValue(type));
        }
        if (!StringUtils.equals(bool, "Y") && !StringUtils.equals(bool, "N")) {
            throw new SootheeException(MyErrorMsg.INCORRECT_VALUE.makeIncorrect(type));
        }
        return true;
    }

    /**
     * score 가 Null 아닌지 확인</hr>
     *
     * @param score Double : 확인할 score
     * @param msg String : Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullScore(Double score, String msg) throws SootheeException {
        if (Objects.isNull(score)) {
            throw new SootheeException(MyErrorMsg.NULL_VALUE.makeValue(msg));
        }
        return true;
    }

    /**
     * score 가 Null 아닌지 확인</hr>
     *
     * @param score Double : 확인할 score
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullScore(Double score) throws SootheeException {
        String msg = "점수 ";
        return checkNullScore(score, msg);
    }

    /**
     * date 가 Null 아닌지 확인</hr>
     *
     * @param date LocalDate : 확인할 date
     * @param msg String : Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullDate(LocalDate date, String msg) throws SootheeException {
        if (Objects.isNull(date)) {
            throw new SootheeException(MyErrorMsg.NULL_VALUE.makeValue(msg));
        }
        return true;
    }

    /**
     * date 가 Null 아닌지 확인</hr>
     *
     * @param date LocalDate : 확인할 date
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullDate(LocalDate date) throws SootheeException {
        String msg = "작성 날짜 ";
        return checkNullDate(date, msg);
    }

    /**
     * 해당 도메인이 Null 아닌지 확인</hr>
     *
     * @param domain Domain : 확인할 Domain
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkNullDomain(Domain domain) throws SootheeException {
        if (Objects.isNull(domain) || Objects.isNull(domain.getId())) {
            throw new SootheeException(MyErrorMsg.NULL_VALUE.makeValue(getMsg(domain, " ")));
        }
        return true;
    }

    /**
     * score 가 양수인지 확인</hr>
     *
     * @param score Double : 확인할 score
     * @param msg String : Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectDouble(Double score, String msg) throws SootheeException {
        if (score < 0) {
            throw new SootheeException(MyErrorMsg.INCORRECT_VALUE.makeIncorrect(msg));
        }
        return true;
    }

    /**
     * score 가 양수인지 확인</hr>
     *
     * @param score Double : 확인할 score
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectDouble(Double score) throws SootheeException {
        String msg = "점수 ";
        return checkCorrectDouble(score, msg);
    }

    /**
     * date 가 2024년 이후이고 2100년 이전인지 확인</hr>
     *
     * @param date LocalDate : 확인할 date
     * @param msg String : Exception의 error message에 적용할 설명
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectLocalDate(LocalDate date, String msg) throws SootheeException {
        if (date.getYear() < 2023 || date.getYear() > 2100) {
            throw new SootheeException(MyErrorMsg.INCORRECT_VALUE.makeIncorrect(msg));
        }
        return true;
    }

    /**
     * date 가 2024년 이후 인지 확인</hr>
     *
     * @param date LocalDate : 확인할 date
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectLocalDate(LocalDate date) throws SootheeException {
        String msg = "작성 날짜 ";
        return checkCorrectLocalDate(date, msg);
    }

    /**
     * 해당 도메인의 id가 양수 인지 확인</hr>
     *
     * @param domain Domain : 확인할 Domain
     * @return 맞으면 true / 아니면 false
     */
    public static boolean checkCorrectDomainId(Domain domain) throws SootheeException {
        if (domain.getId() < 1) {
            throw new SootheeException(MyErrorMsg.INCORRECT_VALUE.makeIncorrect(getMsg(domain, " 일련번호 ")));
        }
        return true;
    }

    /**
     * 상세 설명을 추가한 Exception의 error message에 적용할 설명</hr>
     *
     * @param domain Domain : 확인할 Domain
     * @param msg String : domain type에 추가할 상세 설명
     * @return Exception의 error message에 적용할 domain type + 상세 설명
     */
    private static String getMsg(Domain domain, String msg) {
        return getMsg(domain) + msg;
    }

    /**
     * Exception의 error message에 적용할 설명</hr>
     *
     * @param domain Domain : 확인할 Domain
     * @return 해당 domain type
     */
    private static String getMsg(Domain domain) {
        StringBuilder prefix = new StringBuilder();
        if (domain instanceof Member) {
            prefix.append("회원");
        }
        if (domain instanceof MemberDelReason) {
            prefix.append("회원 탈퇴 사유");
        }
        if (domain instanceof Dairy) {
            prefix.append("일기");
        }
        if (domain instanceof DairyCondition) {
            prefix.append("일기의 컨디션");
        }
        if (domain instanceof Weather) {
            prefix.append("날씨");
        }
        if (domain instanceof Condition) {
            prefix.append("컨디션");
        }
        if (domain instanceof ConditionType) {
            prefix.append("컨디션 타입");
        }
        if (domain instanceof DelReason) {
            prefix.append("탈퇴 사유");
        }
        return prefix.toString();
    }
}
