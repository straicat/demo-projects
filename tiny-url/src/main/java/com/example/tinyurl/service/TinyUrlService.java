package com.example.tinyurl.service;

import com.example.tinyurl.enums.ErrorCode;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.redis.LongUrlCache;
import com.example.tinyurl.redis.ShortUrlBloomFilter;
import com.example.tinyurl.redis.ShortUrlCache;
import com.example.tinyurl.redis.ShortUrlLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TinyUrlService {

    @Resource
    private ShortUrlBloomFilter shortUrlBloomFilter;

    @Resource
    private ShortUrlCache shortUrlCache;

    @Resource
    private LongUrlCache longUrlCache;

    @Resource
    private ShortUrlLock shortUrlLock;

    @Resource
    private TinyUrlDbService tinyUrlDbService;

    @Resource
    private ShortUrlCreatorFactory shortUrlCreatorFactory;

    public String getLongUrl(String shortUrl) {
        // 使用布隆过滤器判断不存在，避免缓存穿透
        if (!shortUrlBloomFilter.contains(shortUrl)) {
            return null;
        }

        // 先查缓存
        String longUrl = shortUrlCache.getLongUrl(shortUrl);
        if (longUrl != null) {
            return longUrl;
        }

        // 使用分布式锁避免缓存击穿
        RLock lock = shortUrlLock.getLock(shortUrl);
        try {
            if (lock != null && lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                longUrl = shortUrlCache.getLongUrl(shortUrl);
                if (longUrl != null) {
                    return longUrl;
                }
                longUrl = tinyUrlDbService.getLongUrl(shortUrl);
                shortUrlCache.set(shortUrl, longUrl);
                return longUrl;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    public String createShortUrl(String longUrl) throws BizException {
        String shortUrl = longUrlCache.getShortUrl(longUrl);
        if (shortUrl != null) {
            return shortUrl;
        }

        ShortUrlCreator shortUrlCreator = shortUrlCreatorFactory.getShortUrlCreator();
        if (shortUrlCreator == null) {
            throw new BizException(ErrorCode.INVALID_CONFIGURATION);
        }
        shortUrl = shortUrlCreator.getShortUrl(longUrl);

        shortUrlBloomFilter.add(shortUrl);
        longUrlCache.set(longUrl, shortUrl);
        shortUrlCache.set(shortUrl, longUrl);

        return shortUrl;
    }
}
