package com.soothee.oauth2.token.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String oauth2ClientId, String refreshToken, long expiration) {
        redisTemplate.opsForValue().set(
                getRedisKey(oauth2ClientId),
                refreshToken,
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String oauth2ClientId) {
        return (String) redisTemplate.opsForValue().get(getRedisKey(oauth2ClientId));
    }

    public void deleteRefreshToken(String oauth2ClientId) {
        redisTemplate.delete(getRedisKey(oauth2ClientId));
    }

    private String getRedisKey(String oauth2ClientId) {
        return "refreshToken:" + oauth2ClientId;
    }
}
