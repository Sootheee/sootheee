package com.soothee.oauth2.controller;

import com.soothee.oauth2.provider.JwtTokenProvider;
import com.soothee.oauth2.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
}
