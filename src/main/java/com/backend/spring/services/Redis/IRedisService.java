package com.backend.spring.services.Redis;

public interface IRedisService {
    void setDataCache(String key, String value, long timeLive);

    void setTimeCacheToLive(String key, long timeLive);

    Object getDataCache(String key);

    void deleteDataCache(String key);

    Long getExpireTime(String key);
}
