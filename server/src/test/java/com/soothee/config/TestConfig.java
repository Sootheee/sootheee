package com.soothee.config;

import com.soothee.dairy.repository.DairyConditionRepository;
import com.soothee.dairy.repository.DairyRepository;
import com.soothee.member.repository.MemberDelReasonRepository;
import com.soothee.member.repository.MemberRepository;
import com.soothee.member.service.MemberDelReasonService;
import com.soothee.reference.repository.ConditionRepository;
import com.soothee.reference.repository.DelReasonRepository;
import com.soothee.reference.repository.WeatherRepository;
import com.soothee.util.CommonTestCode;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public CommonTestCode commonTestCode(WeatherRepository weatherRepository,
                                         ConditionRepository conditionRepository,
                                         DelReasonRepository delReasonRepository,
                                         DairyRepository dairyRepository,
                                         MemberRepository memberRepository,
                                         DairyConditionRepository dairyConditionRepository,
                                         MemberDelReasonRepository memberDelReasonRepository) {
        return new CommonTestCode(weatherRepository,
                                    conditionRepository,
                                    delReasonRepository,
                                    dairyRepository,
                                    memberRepository,
                                    dairyConditionRepository,
                                    memberDelReasonRepository);
    }
}
