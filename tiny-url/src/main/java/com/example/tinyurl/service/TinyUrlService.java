package com.example.tinyurl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.gateway.LeafService;
import com.example.tinyurl.mapper.TinyUrlMappingMapper;
import com.example.tinyurl.model.TinyUrlMapping;
import com.example.tinyurl.util.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class TinyUrlService {
    @Resource
    private TinyUrlMappingMapper tinyUrlMappingMapper;

    @Resource
    private LeafService leafService;

    public String getLongUrl(String shortUrl) {
        QueryWrapper<TinyUrlMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        wrapper.last("limit 1");
        TinyUrlMapping tinyUrlMapping = tinyUrlMappingMapper.selectOne(wrapper);
        return tinyUrlMapping != null ? tinyUrlMapping.getLongUrl() : null;
    }

    public String createShortUrl(String longUrl) throws BizException {
        return createShortUrlByGlobalId(longUrl);
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
}
