package com.soothee.dairy.domain;

import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.reference.domain.Weather;
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
@Import(TestConfig.class)
class DairyTest {
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void updateDairy() {
        //given
        String changeThank = "thanks";
        Dairy savedDairy = commonTestCode.getSavedDairy();
        Weather weather = commonTestCode.getWeather();
        DairyDTO changedInfo = DairyDTO.builder()
                                        .thank(changeThank)
                                        .build();
        //when
        savedDairy.updateDairy(changedInfo, weather);
        //then
        Assertions.assertThat(savedDairy.getThank()).isEqualTo(changeThank);
    }

    @Test
    void deleteDairy() {
        //given
        Dairy savedDairy = commonTestCode.getSavedDairy();
        //when
        savedDairy.deleteDairy();
        //then
        Assertions.assertThat(savedDairy.getIsDelete()).isEqualTo("Y");
    }
}