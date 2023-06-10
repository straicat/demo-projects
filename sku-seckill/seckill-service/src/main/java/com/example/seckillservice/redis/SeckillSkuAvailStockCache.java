package com.example.seckillservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

@Component
@Slf4j
public class SeckillSkuAvailStockCache {

    private static final String KEY_PREFIX = "seckill:seckill-sku-avail-stock:";

    private static final String DECR_STOCK_LUA = "if (redis.call('EXISTS', KEYS[1] == 1)) then\n" +
            "    local stock = tonumber(redis.call('GET', KEYS[1]));\n" +
            "    if (stock - KEYS[2] < 0) then\n" +
            "        return -1;\n" +
            "    end\n" +
            "    redis.call('DECRBY', KEYS[1], KEYS[2]);\n" +
            "    return stock - KEYS[2];\n" +
            "end\n" +
            "return -1;";

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

    public Boolean delete(Long skuId, Long activityId) {
        if (redisClient.delete(KEY_PREFIX + "s" + skuId + "_a" + activityId)) {
            log.info("delete sku availStock cache: skuId={}, activityId={}", skuId, activityId);
            return true;
        } else {
            log.error("delete sku availStock cache fail! skuId={}, activityId={}", skuId, activityId);
            return false;
        }
    }

    public Integer tryDecrStock(Long skuId, Long activityId, Integer buyCnt) {
        Integer stock = redisClient.execute(DECR_STOCK_LUA, Collections.singletonList(KEY_PREFIX + "s" + skuId + "_a" + activityId), buyCnt);
        if (stock != null && stock >= 0) {
            return stock;
        }
        return null;
    }
}
