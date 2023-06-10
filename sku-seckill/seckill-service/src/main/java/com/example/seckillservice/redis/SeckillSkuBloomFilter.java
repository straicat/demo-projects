package com.example.seckillservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class SeckillSkuBloomFilter {

    private static final String KEY_BLOOM = "sekill:seckill-sku-bloom";

    @Resource
    private RedissonClient redissonClient;

    private RBloomFilter<Object> bloomFilter;

    @PostConstruct
    void init() {
        bloomFilter = redissonClient.getBloomFilter(KEY_BLOOM);
        bloomFilter.tryInit(100000000, 0.03);
    }

    public void add(Long skuId, Long activityId) {
        bloomFilter.add("s" + skuId + "_a" + activityId);
    }

    public boolean contains(Long skuId, Long activityId) {
        return bloomFilter.contains("s" + skuId + "_a" + activityId);
    }
}
