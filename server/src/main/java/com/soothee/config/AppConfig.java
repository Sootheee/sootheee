package com.soothee.config;

import com.soothee.common.constants.ConstUrl;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class AppConfig implements WebMvcConfigurer {
    /** Front-Server & Back-Server 간 CORS 설정 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(ConstUrl.getFrontUrl());
    }

    /** Spring-Security 인증/인가 제외 설정 */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/error",
                                                    "/favicon.ico",
                                                    ConstUrl.getResourceCss(),
                                                    ConstUrl.getResourceJs(),
                                                    ConstUrl.getResourceImage());
    }

    /** Spring-Security 설정 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                    .headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                    .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests((requests) -> requests.requestMatchers(new AntPathRequestMatcher(ConstUrl.getLoginPageUrl()),
                                                                                    new AntPathRequestMatcher(ConstUrl.getOnboardingUrl())).permitAll()
                                                                .anyRequest().authenticated())
                    .oauth2Login((configurer) -> configurer.redirectionEndpoint((endpoint) -> endpoint.baseUri(ConstUrl.getBaseLoginUrl()))
                                                            .defaultSuccessUrl(ConstUrl.getHomePageUrl())).build();
    }

    /** Swagger 회원 API 명세서 */
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

    /** Swagger 다이어리 API 명세서 */
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
