package com.soothee.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class appConfig {
    @Bean
    public GroupedOpenApi memberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("member")
                            .pathsToMatch("/member/**")
                            .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().title("Member API")
                                                                                        .description("사용자 관련 처리")
                                                                                        .version("1.0.0")))
                            .build();
    }

    @Bean
    public GroupedOpenApi dairyGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("dairy")
                            .pathsToMatch("/dairy/**")
                            .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().title("Dairy API")
                                                                                        .description("다이어리 관련 처리")
                                                                                        .version("1.0.0")))
                            .build();
    }
}
