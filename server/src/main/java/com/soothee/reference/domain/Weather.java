package com.soothee.reference.domain;

import com.soothee.common.constants.ReferenceType;
import com.soothee.common.domain.Reference;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.custom.valid.SootheeValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference Entity
 * 날씨 조회만 가능 (수정/삭제 불가)
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weather")
public class Weather implements Reference {
    /** 날씨 일련번호 */
    @Id
    private String weatherId;

    /** 날씨 이름 */
    @Column(name = "weather_name", nullable = false, length = 10)
    private String weatherName;

    @Override
    public String getId() {
        return weatherId;
    }

    /**
     * valid
     * 1. 입력된 필수 값 중에 없거나 올바르지 않는 값이 있는 경우 Exception 발생
     */
    public void valid() throws IncorrectValueException, NullValueException {
        SootheeValidation.checkReferenceId(getWeatherId(), ReferenceType.WEATHER);
        SootheeValidation.checkNullForNecessaryString(getWeatherName(), ReferenceType.WEATHER);
    }
}