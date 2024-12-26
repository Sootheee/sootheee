package com.soothee.reference.service;

import com.soothee.common.constants.ReferenceType;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    @Override
    public Weather getWeatherById(String weatherId) throws NullValueException, IncorrectValueException {
        /* 날씨 일련번호로 날씨 정보 조회 */
       Weather result = weatherRepository.findByWeatherId(weatherId)
               /* 해당 날씨 일련번호로 조회된 날씨가 없는 경우 Exception 발생 */
               .orElseThrow(() -> new NullValueException(weatherId, ReferenceType.WEATHER));
       result.valid();
       return result;
    }
}
