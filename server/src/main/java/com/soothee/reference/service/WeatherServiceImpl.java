package com.soothee.reference.service;

import com.soothee.common.constants.DomainType;
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
    public Weather getWeatherById(Long weatherId) throws NullValueException {
       return weatherRepository.findByWeatherId(weatherId)
               .orElseThrow(() -> new NullValueException(weatherId, DomainType.WEATHER));
    }
}
