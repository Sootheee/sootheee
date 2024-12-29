package com.soothee.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.ConstUrl;
import com.soothee.oauth2.filter.JwtAuthenticationFilter;
import com.soothee.oauth2.provider.JwtTokenProvider;
import com.soothee.oauth2.service.DelegatingOAuth2Service;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private final DelegatingOAuth2Service delegatingOAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /** QueryDSL 설정 */
    @PersistenceContext
    private EntityManager entityManager;
    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    /** Front-Server & Back-Server 간 CORS 설정 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(constUrl.getFRONT_URL())
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }

    /** Spring-Security 설정 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((requests) -> requests.requestMatchers(new AntPathRequestMatcher("/swagger-ui.html"),
                                                                                    new AntPathRequestMatcher("/swagger-ui/**"),
                                                                                    new AntPathRequestMatcher("/api/**"),
                                                                                    new AntPathRequestMatcher("/v3/api-docs/**"),
                                                                                    new AntPathRequestMatcher("/docs/**")).permitAll()
                                                                    .anyRequest().authenticated())
                    .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login?error=session-expired")))
                    .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(user -> user.userService(delegatingOAuth2Service))
                                                                            .defaultSuccessUrl("/home", true)
                                                    .successHandler((request, response, authentication) -> {
                                                        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                                                        String jwt = jwtTokenProvider.generateToken(token.getName());
                                                        response.setContentType("application/json");
                                                        response.getWriter().write("{\"accessToken\": \"" + jwt + "\"}");
                                                    })
                                )
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return authenticationConverter;
    }

    /** Swagger 회원 API 명세서 */
    @Bean
    public GroupedOpenApi memberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("member")
                            .pathsToMatch("/member/**")
                            .addOpenApiCustomizer(openApi -> {
                                openApi.info(new io.swagger.v3.oas.models.info.Info().title("Member API")
                                                                                            .description("회원 관련 처리")
                                                                                            .version("1.0.0"));
                                openApi.addSecurityItem(
                                        new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2_auth")
                                );
                            })
                            .build();
    }

    /** Swagger 일기 API 명세서 */
    @Bean
    public GroupedOpenApi dairyGroupedOpenApi() {
        return GroupedOpenApi.builder()
                            .group("dairy")
                            .pathsToMatch("/dairy/**")
                            .addOpenApiCustomizer(openApi -> {
                                openApi.info(new io.swagger.v3.oas.models.info.Info().title("Dairy API")
                                                                                        .description("일기 관련 처리")
                                                                                        .version("1.0.0"));
                                openApi.addSecurityItem(
                                        new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2_auth")
                                );
                            })
                            .build();
    }

    /** Swagger 회원 API 명세서 */
    @Bean
    public GroupedOpenApi statsGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("stats")
                .pathsToMatch("/stats/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new io.swagger.v3.oas.models.info.Info().title("Stats API")
                                                                            .description("통계 관련 처리")
                                                                            .version("1.0.0"));
                    openApi.addSecurityItem(
                            new io.swagger.v3.oas.models.security.SecurityRequirement().addList("oauth2_auth")
                    );
                })
                .build();
    }
}
