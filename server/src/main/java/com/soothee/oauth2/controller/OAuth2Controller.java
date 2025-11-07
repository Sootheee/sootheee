package com.soothee.oauth2.controller;

import com.soothee.common.constants.SnsType;
import com.soothee.oauth2.provider.JwtTokenProvider;
import com.soothee.oauth2.token.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {
    private static final Logger log = LoggerFactory.getLogger(OAuth2Controller.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${oauth2.kakao.logout.url}")
    private String kakaoLogoutUrl;

    @Value("${oauth2.kakao.bearer.auth}")
    private String bearerAuth;

    @Value("${oauth2.google.logout.url}")
    private String googleLogoutUrl;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            return new ResponseEntity<>("Invalid or expired Refresh Token", HttpStatus.UNAUTHORIZED);
        }

        String oauth2ClientId = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String storedRefreshToken = refreshTokenRepository.getRefreshToken(oauth2ClientId);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return new ResponseEntity<>("Invalid or expired Refresh Token", HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = jwtTokenProvider.generateToken(oauth2ClientId);
        return new ResponseEntity<>(Map.of("accessToken", newAccessToken), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String accessToken = jwtTokenProvider.generateToken(token.getName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(token.getName());
        refreshTokenRepository.saveRefreshToken(authentication.getName(), refreshToken, JwtTokenProvider.REFRESH_EXPIRATION_TIME);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge((int) JwtTokenProvider.REFRESH_EXPIRATION_TIME / 1000)
                .build();
        return ResponseEntity.ok()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("message", "Authentication successful"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            String oauth2ClientId = authentication.getName();

            // Refresh Token 삭제
            refreshTokenRepository.deleteRefreshToken(oauth2ClientId);

            // SNS 로그아웃 처리
            if (StringUtils.equals(SnsType.KAKAOTALK.toString(),registrationId)) {
                handleKakaoLogout();
            }
            if (StringUtils.equals(SnsType.GOOGLE.toString(), registrationId)) {
                try {
                    handleGoogleLogout(response);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("message", "Logout successful"));
    }


    private void handleKakaoLogout() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerAuth);

        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.postForEntity(kakaoLogoutUrl, request, String.class);
    }

    private void handleGoogleLogout(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleLogoutUrl);
    }
}
