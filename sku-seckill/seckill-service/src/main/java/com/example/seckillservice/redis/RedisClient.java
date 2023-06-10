package com.example.seckillservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisClient {

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

    public Boolean delete(String key) {
        if (key != null) {
            return stringRedisTemplate.delete(key);
        }
        return false;
    }

    public <T> T execute(String script, List<String> keys, Object... args) {
        RedisScript<T> redisScript = new DefaultRedisScript<>(script);
        return stringRedisTemplate.execute(redisScript, keys, args);
    }
}
