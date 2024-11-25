package com.soothee.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class ConstUrl {
    /** Front Server Base URL */
    @Value("${soothee.front.url}")
    private String FRONT_URL;
    /** OAuth2 로그인 Base URL */
    private final String LOGIN_BASE_URL = "/login/oauth2/callback/";
    /** 로그인 페이지 URL */
    private final String LOGIN_PAGE_URL = "/login";
    /** 로그인 성공 URL */
    private final String LOGIN_SUCCESS_URL = "/auth/success";
    /** 온보딩 페이지 URL */
    private final String ONBOARDING_URL = "/onBoarding";
    /** 홈 URL */
    private final String HOMEPAGE_URL = "/home";
    /** Front CSS Path */
    private final String CSS = "/css";
    /** Front JS Path */
    private final String JS = "/js";
    /** Front IMAGE Path */
    private final String IMAGE = "/images";
}
