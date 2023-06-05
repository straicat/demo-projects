package com.example.tinyurl.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class ShortUrlCache {

    private static final String KEY_PREFIX_SHORT_URL_CACHE = "tiny-url:short-url-cache:";

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Resource
    private RedisClient redisClient;

    public void set(String shortUrl, String longUrl) {
        redisClient.set(KEY_PREFIX_SHORT_URL_CACHE + shortUrl, longUrl, 24 * 3600L + random.nextInt(600));
    }

    public String getLongUrl(String shortUrl) {
        return (String) redisClient.get(KEY_PREFIX_SHORT_URL_CACHE + shortUrl);
    }
}
