package com.soothee.reference.service;

import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.Weather;

public interface WeatherService {
    /**
     * 날씨 일련번호로 날씨 정보 조회
     *
     * @param weatherId 조회할 날씨 일련번호
     * @return 조회된 날씨 정보
     */
    Weather getWeatherById(String weatherId) throws NullValueException;
}
