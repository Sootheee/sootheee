package com.soothee.config;

import com.soothee.common.constants.ConstUrl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "oauth2_auth"),
        info = @Info(
                title = "SOOTHEE",
                version = "1.0",
                description = "Soothee API DOC"
        )
)
@SecurityScheme(
        name = "oauth2_auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "https://auth-server.com/oauth/authorize",
                        tokenUrl = "https://auth-server.com/oauth/token",
                        scopes = {
                                @OAuthScope(name = "read", description = "Read access"),
                                @OAuthScope(name = "write", description = "Write access")
                        }
                )
        )
)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {
    private final ConstUrl constUrl;

    /** Front-Server & Back-Server 간 CORS 설정 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(constUrl.getFRONT_URL());
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
                    .authorizeHttpRequests((requests) -> requests.requestMatchers(new AntPathRequestMatcher("/login"),
                                                                                    new AntPathRequestMatcher("/onBoarding"),
                                                                                    new AntPathRequestMatcher("/error/**"),
                                                                                    new AntPathRequestMatcher("/status"),
                                                                                    new AntPathRequestMatcher("/css"),
                                                                                    new AntPathRequestMatcher("/js"),
                                                                                    new AntPathRequestMatcher("/images"),
                                                                                    new AntPathRequestMatcher("/swagger-ui/**"),
                                                                                    new AntPathRequestMatcher("/api"),
                                                                                    new AntPathRequestMatcher("/v3/**"),
                                                                                    new AntPathRequestMatcher("/docs")).permitAll()
                                                                .anyRequest().authenticated())
                    .oauth2Login((configurer) -> configurer.redirectionEndpoint((endpoint) -> endpoint.baseUri("/login/oauth2/callback/"))
                                                            .defaultSuccessUrl("/home")).build();
    }

    /** Swagger 회원 API 명세서 */
    @Bean
    public GroupedOpenApi memberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("member")
                            .pathsToMatch("/member/**")
                            .addOpenApiCustomizer(openApi -> {
                                openApi.setInfo(new io.swagger.v3.oas.models.info.Info().title("Member API")
                                                                                            .description("회원 관련 처리")
                                                                                            .version("1.0.0"));
                                openApi.addSecurityItem(
                                        new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2_auth")
                                );
                            })
                            .build();
    }

    /** Swagger 다이어리 API 명세서 */
    @Bean
    public GroupedOpenApi dairyGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("dairy")
                            .pathsToMatch("/dairy/**")
                            .addOpenApiCustomizer(openApi -> {
                                openApi.setInfo(new io.swagger.v3.oas.models.info.Info().title("Dairy API")
                                                                                        .description("다이어리 관련 처리")
                                                                                        .version("1.0.0"));
                                openApi.addSecurityItem(
                                        new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2_auth")
                                );
                            })
                            .build();
    }
}
