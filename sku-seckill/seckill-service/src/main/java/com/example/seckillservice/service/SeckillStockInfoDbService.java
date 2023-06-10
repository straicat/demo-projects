package com.example.seckillservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckillservice.mapper.SeckillStockInfoMapper;
import com.example.seckillservice.model.SeckillStockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class SeckillStockInfoDbService {

    @Resource
    private SeckillStockInfoMapper seckillStockInfoMapper;

    public SeckillStockInfo getStockInfo(Long skuId, Long activityId) {
        QueryWrapper<SeckillStockInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuId)
                .eq("activity_id", activityId);
        wrapper.last("limit 1");
        return seckillStockInfoMapper.selectOne(wrapper);
    }
}
