package com.example.seckillservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SeckillSkuAvailStockCache {

    private static final String KEY_PREFIX = "seckill:seckill-sku-avail-stock:";

    @Resource
    private RedisClient redisClient;

    public void set(Long skuId, Long activityId, Integer availStock) {
        set(skuId, activityId, availStock, 0);
    }

    public void set(Long skuId, Long activityId, Integer availStock, Integer timeout) {
        log.info("set sku availStock cache: skuId={}, activityId={}, availStock={}, timeout={}", skuId, activityId, availStock, timeout);
        redisClient.set(KEY_PREFIX + "s" + skuId + "_a" + activityId, String.valueOf(availStock), timeout);
    }

    public Integer get(Long skuId, Long activityId) {
        Object obj = redisClient.get(KEY_PREFIX + "s" + skuId + "_a" + activityId);
        if (obj != null) {
            String availStock = obj.toString();
            if (StringUtils.isNotEmpty(availStock)) {
                return Integer.valueOf(availStock);
            }
        }
        return null;
    }

    public void delete(Long skuId, Long activityId) {
        redisClient.delete(KEY_PREFIX + "s" + skuId + "_a" + activityId);
        log.info("delete sku stock cache: skuId={}", skuId);
    }
}
