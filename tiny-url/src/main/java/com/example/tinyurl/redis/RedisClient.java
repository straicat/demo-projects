package com.example.tinyurl.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisClient {

    // 若使用RedisTemplate会出现\xac\xed\x00\x05t\x00
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout) {
        if (timeout > 0) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    public Object get(String key) {
        return key != null ? stringRedisTemplate.opsForValue().get(key) : null;
    }
}
