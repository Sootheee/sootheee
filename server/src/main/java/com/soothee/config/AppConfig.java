package com.soothee.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soothee.common.constants.SnsType;
import com.soothee.oauth2.filter.JwtAuthenticationFilter;
import com.soothee.oauth2.provider.JwtTokenProvider;
import com.soothee.oauth2.service.DelegatingOAuth2Service;
import com.soothee.oauth2.token.repository.RefreshTokenRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;


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
                .allowedOrigins("http://localhost:3000", "frontUrl")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                .exposedHeaders("Authorization", "Content-Disposition")
                .allowCredentials(true);
    }

    /** redis connect */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /** refresh Token redis setting */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
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
                                                                                String accessToken = jwtTokenProvider.generateToken(token.getName());
                                                                                String refreshToken = jwtTokenProvider.generateRefreshToken(token.getName());
                                                                                Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
                                                                                accessTokenCookie.setHttpOnly(true);
                                                                                accessTokenCookie.setSecure(true);
                                                                                accessTokenCookie.setPath("/");
                                                                                accessTokenCookie.setMaxAge(3600);

                                                                                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                                                                                refreshTokenCookie.setHttpOnly(true);
                                                                                refreshTokenCookie.setSecure(true);
                                                                                refreshTokenCookie.setPath("/");
                                                                                refreshTokenCookie.setMaxAge((int) JwtTokenProvider.REFRESH_EXPIRATION_TIME / 1000);

                                                                                response.addCookie(accessTokenCookie);
                                                                                response.addCookie(refreshTokenCookie);

                                                                                response.setContentType("application/json");
                                                                                response.getWriter().write("{\"message\": \"Login successful\"}");
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
