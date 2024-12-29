package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.ReferenceType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(Class<? extends CustomType> classType, String value) {
        super(value + (Objects.equals(classType, ContentType.class) ? "은 type path paremeter의 올바른 값이 아닙니다."
                : (Objects.equals(classType, ReferenceType.class) ? "는 올바르지 않는 일련번호 형식입니다."
                : "은 order_by query parameter의 올바른 값이 아닙니다.")));
    }

    public IncorrectParameterException(String value, boolean yn) {
        super(yn ? "path parameter {" + value + "}의 값이 \"name\"이 아닙니다."
                : "query parameter name의 값{" + value + "}이 올바르지 않습니다.");
    }

    public IncorrectParameterException(OAuth2UserRequest request) {
        super(request.getClientRegistration().getRegistrationId() + "은 SNS OAuth2 로그인을 지원하지 않습니다.");
    }

    public IncorrectParameterException(int year, int weekOrMonth, boolean yn) {
        super(yn ? "path parameter year{" + year + "}와 month{" + weekOrMonth + "}가 올바르지 않습니다."
                : "path parameter year{" + year + "} week{" + weekOrMonth + "}가 올바르지 않습니다.");
    }

    public IncorrectParameterException(LocalDate date) {
        super("path parameter date{" + date + "}가 올바르지 않습니다.");
    }

    public IncorrectParameterException(Long id, boolean yn) {
        super(yn ? "path parameter dairy_id{" + id + "}가 올바르지 않습니다."
                : "path parameter member_id{" + id + "}가 올바르지 않습니다.");
    }
}
