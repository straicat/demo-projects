package com.example.tinyurl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tinyurl.mapper.TinyUrlMappingMapper;
import com.example.tinyurl.model.TinyUrlMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class TinyUrlDbService {

    @Resource
    private TinyUrlMappingMapper tinyUrlMappingMapper;

    public String getLongUrl(String shortUrl) {
        QueryWrapper<TinyUrlMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("short_url", shortUrl);
        wrapper.last("limit 1");
        // 如果查询结果有多条，selectOne会抛异常
        TinyUrlMapping tinyUrlMapping = tinyUrlMappingMapper.selectOne(wrapper);
        return tinyUrlMapping != null ? tinyUrlMapping.getLongUrl() : null;
    }

    public boolean addUrl(String shortUrl, String longUrl) {
        TinyUrlMapping entity = new TinyUrlMapping();
        entity.setShortUrl(shortUrl);
        entity.setLongUrl(longUrl);
        entity.setCtime(System.currentTimeMillis());
        return tinyUrlMappingMapper.insert(entity) > 0;
    }
}
