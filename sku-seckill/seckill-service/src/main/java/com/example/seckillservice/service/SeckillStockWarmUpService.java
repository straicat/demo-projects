package com.example.seckillservice.service;

import com.example.seckillservice.model.SeckillStockInfo;
import com.example.seckillservice.redis.SeckillSkuAvailStockCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class SeckillStockWarmUpService {

    @Resource
    private SeckillSkuAvailStockCache seckillSkuAvailStockCache;

    public void warmUp(SeckillStockInfo stockInfo) {
        if (stockInfo == null) {
            return;
        }
        Integer availStock = stockInfo.getStock() - stockInfo.getLockStock();
        seckillSkuAvailStockCache.set(stockInfo.getSkuId(), stockInfo.getActivityId(), availStock);
    }
}
