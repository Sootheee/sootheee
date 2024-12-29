package com.soothee.dairy.domain;

import com.soothee.common.constants.BooleanYN;
import com.soothee.config.TestConfig;
import com.soothee.dairy.service.command.DairyModify;
import com.soothee.util.CommonTestCode;
import com.soothee.reference.domain.Weather;
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
class DairyTest {
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void updateDairy() {
        //given
        String changeThank = "thanks";
        Dairy savedNewDairy = commonTestCode.saveNewDairy();
        Weather weather = commonTestCode.getWeather();
        DairyModify changedInfo = DairyModify.builder()
                                                .dairyId(savedNewDairy.getDairyId())
                                                .date(savedNewDairy.getDate())
                                                .weatherId(weather.getWeatherId())
                                                .score(savedNewDairy.getScore())
                                                .thank(changeThank)
                                                .build();
        //when
        savedNewDairy.updateDairy(changedInfo, weather);
        //then
        Assertions.assertThat(savedNewDairy.getThank()).isEqualTo(changeThank);
    }

    @Test
    void deleteDairy() {
        //given
        Dairy savedDairy = commonTestCode.getSavedDairy(CommonTestCode.DAIRY_ID1);
        //when
        savedDairy.deleteDairy();
        //then
        Assertions.assertThat(savedDairy.getIsDelete()).isEqualTo(BooleanYN.Y);
    }
}