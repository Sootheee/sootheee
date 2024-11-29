package com.soothee.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
@ConfigurationProperties(prefix = "soothee")
public class ConstUrl {
    /** Front Server Base URL */
    private String FRONT_URL;
}
