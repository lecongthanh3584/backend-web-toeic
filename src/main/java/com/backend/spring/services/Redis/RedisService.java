package com.backend.spring.services.Redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService implements IRedisService {

    protected final RedisTemplate<String, Object> redisTemplate;

    protected final ObjectMapper redisObjectMapper;

    @Override
    public void setDataCache(String key, String value, long timeLive) {
        redisTemplate.opsForValue().set(key, value, timeLive, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setTimeCacheToLive(String key, long timeLive) {
        redisTemplate.expire(key, timeLive, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object getDataCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteDataCache(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }
}
