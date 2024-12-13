package com.soothee.reference.service;

import com.soothee.common.exception.MyErrorMsg;
import com.soothee.common.exception.MyException;
import com.soothee.reference.domain.Weather;
import com.soothee.reference.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    @Override
    public Weather getWeatherById(Long weatherId) {
       return weatherRepository.findByWeatherId(weatherId)
               .orElseThrow(() -> new MyException(HttpStatus.INTERNAL_SERVER_ERROR, MyErrorMsg.NULL_VALUE));
    }
}
