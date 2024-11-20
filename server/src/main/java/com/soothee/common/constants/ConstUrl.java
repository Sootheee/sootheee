package com.soothee.common.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "soothee")
public class ConstUrl {
    /** Front Server Base URL */
    private static String FRONT_URL;
    /** OAuth2 로그인 Base URL */
    private static String BASE_LOGIN_URL;
    /** 온보딩 페이지 URL */
    private static String ONBOARDING_URL;
    /** 로그인 페이지 URL */
    private static String LOGIN_PAGE_URL;
    /** 홈 URL */
    private static String HOME_PAGE_URL;

    public static String getFrontUrl() {
        return FRONT_URL;
    }

    public static String getBaseLoginUrl() {
        return BASE_LOGIN_URL;
    }

    public static String getOnboardingUrl() {
        return ONBOARDING_URL;
    }

    public static String getLoginPageUrl() {
        return LOGIN_PAGE_URL;
    }

    public static String getHomePageUrl() {
        return HOME_PAGE_URL;
    }
}
