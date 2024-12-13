package com.soothee.reference.service;

import com.soothee.reference.domain.Weather;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
class WeatherServiceTest {
    @Autowired
    private WeatherService weatherService;

    @Test
    void getWeatherById() {
        Weather weather = weatherService.getWeatherById(1L);
        Assertions.assertThat(weather.getWeatherId()).isEqualTo(1L);
    }
}