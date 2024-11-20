package com.soothee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "soothee")
public class ConstUrl {
    private static String FRONT_URL;
    private static String BASE_LOGIN_URL;

    public static String getFrontUrl() {
        return FRONT_URL;
    }

    public static String getBaseLoginUrl() {
        return BASE_LOGIN_URL;
    }
}
