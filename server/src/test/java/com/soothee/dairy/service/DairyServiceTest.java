package com.soothee.dairy.service;

import com.soothee.config.TestConfig;
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
class DairyServiceTest {
    @Autowired
    private DairyService dairyService;


    @Test
    void getAllDairyMonthly() {
        //given
        //when
        //then
    }

    @Test
    void getDairyByDate() {
        //given
        //when
        //then
    }

    @Test
    void getDairyByDairyId() {
        //given
        //when
        //then
    }

    @Test
    void registerDairy() {
        //given
        //when
        //then
    }

    @Test
    void modifyDairy() {
        //given
        //when
        //then
    }

    @Test
    void deleteDairy() {
        //given
        //when
        //then
    }
}