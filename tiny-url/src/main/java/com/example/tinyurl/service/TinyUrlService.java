package com.example.tinyurl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tinyurl.enums.ShortUrlGenMethodEnum;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.gateway.LeafService;
import com.example.tinyurl.mapper.TinyUrlMappingMapper;
import com.example.tinyurl.model.TinyUrlMapping;
import com.example.tinyurl.redis.RedisClient;
import com.example.tinyurl.util.Base62Encoder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TinyUrlService {
    @Resource
    private TinyUrlMappingMapper tinyUrlMappingMapper;

    @Resource
    private LeafService leafService;

    @Resource
    private RedisClient redisClient;

    @Resource
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX_SHORT_URL_CACHE = "tiny-url:short-url-cache:";
    private static final String KEY_PREFIX_LONG_URL_CACHE = "tiny-url:long-url-cache:";
    private static final String KEY_SHORT_URL_BLOOM = "tiny-url:short-url-bloom";
    private static final String KEY_PREFIX_SHORT_URL_LOCK = "tiny-url:short-url-lock:";

    private RBloomFilter<Object> bloomFilter;
    private ThreadLocalRandom random;

    @Value("${shorten.method}")
    private Integer shortenMethod;

    @PostConstruct
    void init() {
        bloomFilter = redissonClient.getBloomFilter(KEY_SHORT_URL_BLOOM);
        bloomFilter.tryInit(100000000, 0.03);
        random = ThreadLocalRandom.current();
    }

    private Long getUrlCacheTimeout() {
        return 24 * 3600L + random.nextInt(600);
    }

    public String getLongUrl(String shortUrl) {
        // 使用布隆过滤器判断不存在，避免缓存穿透
        if (!bloomFilter.contains(shortUrl)) {
            return null;
        }

        // 先查缓存
        String longUrl = (String) redisClient.get(KEY_PREFIX_SHORT_URL_CACHE + shortUrl);
        if (longUrl != null) {
            return longUrl;
        }

        // 使用分布式锁避免缓存击穿
        RLock shortUrlLock = redissonClient.getLock(KEY_PREFIX_SHORT_URL_LOCK + shortUrl);
        try {
            if (shortUrlLock != null && shortUrlLock.tryLock(5, 30, TimeUnit.SECONDS)) {
                longUrl = (String) redisClient.get(KEY_PREFIX_SHORT_URL_CACHE + shortUrl);
                if (longUrl != null) {
                    return longUrl;
                }
                longUrl = getLongUrlFromDb(shortUrl);
                redisClient.set(KEY_PREFIX_SHORT_URL_CACHE + shortUrl, longUrl, getUrlCacheTimeout());
                return longUrl;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (shortUrlLock != null && shortUrlLock.isLocked() && shortUrlLock.isHeldByCurrentThread()) {
                shortUrlLock.unlock();
            }
        }
        return null;
    }

    private String getLongUrlFromDb(String shortUrl) {
        QueryWrapper<TinyUrlMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        wrapper.last("limit 1");
        // 如果查询结果有多条，selectOne会抛异常
        TinyUrlMapping tinyUrlMapping = tinyUrlMappingMapper.selectOne(wrapper);
        return tinyUrlMapping != null ? tinyUrlMapping.getLongUrl() : null;
    }

    public String createShortUrl(String longUrl) throws BizException {
        String shortUrl = (String) redisClient.get(KEY_PREFIX_LONG_URL_CACHE + longUrl);
        if (shortUrl != null) {
            return shortUrl;
        }

        if (Objects.equals(shortenMethod, ShortUrlGenMethodEnum.GLOBAL_ID.getCode())) {
            shortUrl = createShortUrlByGlobalId(longUrl);
        } else if (Objects.equals(shortenMethod, ShortUrlGenMethodEnum.HASH.getCode())) {
            shortUrl = createShortUrlByHash(longUrl);
        } else {
            throw BizException.INVALID_CONFIGURATION;
        }

        bloomFilter.add(shortUrl);
        redisClient.set(KEY_PREFIX_LONG_URL_CACHE + longUrl, shortUrl, getUrlCacheTimeout());
        redisClient.set(KEY_PREFIX_SHORT_URL_CACHE + shortUrl, longUrl, getUrlCacheTimeout());

        return shortUrl;
    }

    public String createShortUrlByGlobalId(String longUrl) throws BizException {
        Long globalId = leafService.getId();
        if (globalId == null) {
            throw BizException.GLOBAL_ID_GENERATE_FAIL;
        }
        if (globalId >= (62L ^ 8)) {
            throw BizException.GLOBAL_ID_OVERFLOW;
        }
        String shortUrl = Base62Encoder.encode(globalId);
        TinyUrlMapping entity = new TinyUrlMapping();
        entity.setShortUrl(shortUrl);
        entity.setLongUrl(longUrl);
        entity.setCtime(System.currentTimeMillis());
        tinyUrlMappingMapper.insert(entity);
        return shortUrl;
    }

    public String createShortUrlByHash(String longUrl) {
        TinyUrlMapping entity = new TinyUrlMapping();
        entity.setLongUrl(longUrl);
        entity.setCtime(System.currentTimeMillis());
        String shortUrl;
        shortUrl = getShortUrlByHash(longUrl);

        while (true) {
            // 使用布隆过滤器判断Hash键是否可能重复
            if (!bloomFilter.contains(shortUrl)) {
                entity.setShortUrl(shortUrl);
                tinyUrlMappingMapper.insert(entity);
                break;
            } else {
                shortUrl = getShortUrlByHash(longUrl + random.nextInt());
            }
        }
        return shortUrl;
    }

    private String getShortUrlByHash(String longUrl) {
        HashFunction hashFunction = Hashing.murmur3_128();
        long hashCode = hashFunction.hashString(longUrl, StandardCharsets.UTF_8).asLong();
        // 令短URL长度不超过8，则可以存储62^8个短URL，该数小于long的数量
        // 考虑到 2^47 < 62^8 < 2^48 ，截取long的47位即可保证生成的短URL长度不超过8
        hashCode &= 0x7fffffffffffL;
        return Base62Encoder.encode(hashCode);
    }
}
