package com.example.tinyurl.service.impl;

import com.example.tinyurl.enums.ErrorCode;
import com.example.tinyurl.enums.ShortenMethodEnum;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.redis.ShortUrlBloomFilter;
import com.example.tinyurl.service.ShortUrlCreator;
import com.example.tinyurl.service.TinyUrlDbService;
import com.example.tinyurl.util.Base62Encoder;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class HashShortUrlCreatorImpl implements ShortUrlCreator {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Resource
    private ShortUrlBloomFilter shortUrlBloomFilter;

    @Resource
    private TinyUrlDbService tinyUrlDbService;

    @Override
    public String getShortUrl(String longUrl) throws BizException {
        String shortUrl = getShortUrlByHash(longUrl);

        while (true) {
            // 使用布隆过滤器判断Hash键是否可能重复
            if (!shortUrlBloomFilter.contains(shortUrl)) {
                if (!tinyUrlDbService.addUrl(shortUrl, longUrl)) {
                    throw new BizException(ErrorCode.ADD_RECORD_EXCEPTION);
                }
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

    @Override
    public Integer getShortenMethod() {
        return ShortenMethodEnum.HASH.getCode();
    }
}
