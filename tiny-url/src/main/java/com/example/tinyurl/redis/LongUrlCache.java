package com.example.tinyurl.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class LongUrlCache {

    private static final String KEY_PREFIX_LONG_URL_CACHE = "tiny-url:long-url-cache:";

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Resource
    private RedisClient redisClient;

    public void set(String longUrl, String shortUrl) {
        redisClient.set(KEY_PREFIX_LONG_URL_CACHE + longUrl, shortUrl, 24 * 3600L + random.nextInt(600));
    }

    public String getShortUrl(String longUrl) {
        return (String) redisClient.get(KEY_PREFIX_LONG_URL_CACHE + longUrl);
    }
}
