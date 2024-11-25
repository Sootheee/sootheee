package com.soothee.common.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "soothee")
public class ConstUrl {
    /** Front Server Base URL */
    public static String FRONT_URL;
    /** OAuth2 로그인 Base URL */
    public static String BASE_LOGIN_URL;
    /** 온보딩 페이지 URL */
    public static String ONBOARDING_URL;
    /** 로그인 페이지 URL */
    public static String LOGIN_PAGE_URL;
    /** 로그인 성공 URL */
    public static String LOGIN_SUCCESS_URL;
    /** 홈 URL */
    public static String HOMEPAGE_URL;
    /** Front CSS Path */
    public static String RESOURCE_CSS;
    /** Front JS Path */
    public static String RESOURCE_JS;
    /** Front IMAGE Path */
    public static String RESOURCE_IMAGE;
}
