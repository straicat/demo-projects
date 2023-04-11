package com.example.tinyurl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.gateway.LeafService;
import com.example.tinyurl.mapper.TinyUrlMappingMapper;
import com.example.tinyurl.model.TinyUrlMapping;
import com.example.tinyurl.redis.RedisClient;
import com.example.tinyurl.util.Base62Encoder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class TinyUrlService {
    @Resource
    private TinyUrlMappingMapper tinyUrlMappingMapper;

    @Resource
    private LeafService leafService;

    @Resource
    private RedisClient redisClient;

    private static final Long URL_CACHE_TIMEOUT = 24 * 3600L;

    private static final Random RANDOM = new Random();

    @Value("${shorten.method}")
    private Integer shortenMethod;

    public String getLongUrl(String shortUrl) {
        QueryWrapper<TinyUrlMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        wrapper.last("limit 1");
        // 如果查询结果有多条，selectOne会抛异常
        TinyUrlMapping tinyUrlMapping = tinyUrlMappingMapper.selectOne(wrapper);
        return tinyUrlMapping != null ? tinyUrlMapping.getLongUrl() : null;
    }

    public String createShortUrl(String longUrl) throws BizException {
        String shortUrl = (String) redisClient.get(longUrl);
        if (shortUrl != null) {
            return shortUrl;
        }

        if (Objects.equals(shortenMethod, 1)) {
            shortUrl = createShortUrlByGlobalId(longUrl);
        } else if (Objects.equals(shortenMethod, 2)) {
            shortUrl = createShortUrlByHash(longUrl);
        } else {
            throw BizException.INVALID_CONFIGURATION;
        }

        redisClient.set(longUrl, shortUrl, URL_CACHE_TIMEOUT);
        return shortUrl;
    }

    public String createShortUrlByGlobalId(String longUrl) throws BizException {
        Long globalId = leafService.getId();
        if (globalId == null) {
            throw BizException.GLOBAL_ID_GENERATE_FAIL;
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
        try {
            entity.setShortUrl(shortUrl);
            tinyUrlMappingMapper.insert(entity);
        } catch (DuplicateKeyException e) {
            while (true) {
                shortUrl = getShortUrlByHash(longUrl + RANDOM.nextInt());
                entity.setShortUrl(shortUrl);
                try {
                    tinyUrlMappingMapper.insert(entity);
                    break;
                } catch (DuplicateKeyException ignore) {
                }
            }
        }
        return shortUrl;
    }

    private String getShortUrlByHash(String longUrl) {
        HashFunction hashFunction = Hashing.murmur3_128();
        byte[] bytes = hashFunction.hashString(longUrl, StandardCharsets.UTF_8).asBytes();
        long hashCode = 0;
        for (int i = 0; i < 8; i++) {
            hashCode |= ((long) (bytes[i] & 0xff) << (i * 8));
        }
        // 令短URL长度不超过8，则可以存储62^8个短URL，该数小于long的数量
        // 考虑到 2^47 < 62^8 < 2^48 ，截取long的47位即可保证生成的短URL长度不超过8
        hashCode &= 0x7fffffffffffL;
        return Base62Encoder.encode(hashCode);
    }
}
