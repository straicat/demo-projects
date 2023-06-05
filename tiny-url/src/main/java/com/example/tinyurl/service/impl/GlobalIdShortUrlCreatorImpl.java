package com.example.tinyurl.service.impl;

import com.example.tinyurl.enums.ErrorCode;
import com.example.tinyurl.enums.ShortUrlShortenMethodEnum;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.gateway.LeafService;
import com.example.tinyurl.service.ShortUrlCreator;
import com.example.tinyurl.service.TinyUrlDbService;
import com.example.tinyurl.util.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class GlobalIdShortUrlCreatorImpl implements ShortUrlCreator {

    private static final Long GLOBAL_ID_OVERFLOW = 218340105584896L;
    @Resource
    private LeafService leafService;
    @Resource
    private TinyUrlDbService tinyUrlDbService;

    @Override
    public String getShortUrl(String longUrl) throws BizException {
        Long globalId = leafService.getId();
        if (globalId == null) {
            throw new BizException(ErrorCode.GLOBAL_ID_GENERATE_FAIL);
        }
        if (globalId >= GLOBAL_ID_OVERFLOW) {
            throw new BizException(ErrorCode.GLOBAL_ID_OVERFLOW);
        }
        String shortUrl = Base62Encoder.encode(globalId);
        if (!tinyUrlDbService.addUrl(shortUrl, longUrl)) {
            throw new BizException(ErrorCode.ADD_RECORD_EXCEPTION);
        }
        return shortUrl;
    }

    @Override
    public Integer getShortenMethod() {
        return ShortUrlShortenMethodEnum.GLOBAL_ID.getCode();
    }
}
