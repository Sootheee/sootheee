package com.soothee.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "soothee")
public class ConstUrl {
    /** Front Server Base URL */
    private String frontUrl;
    /** OAuth2 로그인 Base URL */
    private String loginBaseUrl;
    /** 온보딩 페이지 URL */
    private String onboardingUrl;
    /** 로그인 페이지 URL */
    private String loginPageUrl;
    /** 로그인 성공 URL */
    private String loginSuccessUrl;
    /** 홈 URL */
    private String homepageUrl;
    /** Front CSS Path */
    private String resourceCss;
    /** Front JS Path */
    private String resourceJs;
    /** Front IMAGE Path */
    private String resourceImage;
}
