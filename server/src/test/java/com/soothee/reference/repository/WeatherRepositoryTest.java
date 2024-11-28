package com.soothee.reference.repository;

import com.soothee.reference.domain.Weather;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeatherRepositoryTest {
    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    void findOne() {
        Weather weather = weatherRepository.findByWeatherId(1L).orElseThrow();
        Assertions.assertThat(weather).isNotNull();
    }
}