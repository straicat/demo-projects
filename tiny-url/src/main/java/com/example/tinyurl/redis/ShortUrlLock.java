package com.example.tinyurl.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ShortUrlLock {

    private static final String KEY_PREFIX_SHORT_URL_LOCK = "tiny-url:short-url-lock:";

    @Resource
    private RedissonClient redissonClient;

    public RLock getLock(String shortUrl) {
        return redissonClient.getLock(KEY_PREFIX_SHORT_URL_LOCK + shortUrl);
    }
}
