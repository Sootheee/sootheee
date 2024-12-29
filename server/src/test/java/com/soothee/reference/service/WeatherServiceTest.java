package com.soothee.reference.service;

import com.soothee.config.TestConfig;
import com.soothee.custom.exception.NullValueException;
import com.soothee.reference.domain.Weather;
import com.soothee.util.CommonTestCode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class WeatherServiceTest {
    @Autowired
    private WeatherService weatherService;

    @Test
    void getWeatherById() {
        try {
            //given
            //when
            Weather weather = weatherService.getWeatherById(CommonTestCode.WEATHER_ID);
            //then
            Assertions.assertThat(weather.getWeatherName()).isEqualTo("sunny");
        } catch (NullValueException e) {
            log.error("\n", e);
        }
    }
}