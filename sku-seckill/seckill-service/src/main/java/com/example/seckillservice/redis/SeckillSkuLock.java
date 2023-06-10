package com.example.seckillservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SeckillSkuLock {

    private static final String KEY_PREFIX = "seckill:seckill-sku-lock:";

    @Resource
    private RedissonClient redissonClient;

    public RLock getLock(Long skuId, Long activityId) {
        return redissonClient.getLock(KEY_PREFIX + "s" + skuId + "_a" + activityId);
    }
}
