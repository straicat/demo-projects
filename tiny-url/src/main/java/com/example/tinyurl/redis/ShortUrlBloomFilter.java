package com.example.tinyurl.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class ShortUrlBloomFilter {

    private static final String KEY_SHORT_URL_BLOOM = "tiny-url:short-url-bloom";

    @Resource
    private RedissonClient redissonClient;

    private RBloomFilter<Object> bloomFilter;

    @PostConstruct
    void init() {
        bloomFilter = redissonClient.getBloomFilter(KEY_SHORT_URL_BLOOM);
        bloomFilter.tryInit(100000000, 0.03);
    }

    public void add(String shortUrl) {
        bloomFilter.add(shortUrl);
    }

    public boolean contains(String shortUrl) {
        return bloomFilter.contains(shortUrl);
    }
}
