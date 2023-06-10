package com.example.seckillservice.service;

import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.SeckillSkuStockResponse;
import com.example.seckillservice.model.SeckillStockInfo;
import com.example.seckillservice.redis.SeckillSkuAvailStockCache;
import com.example.seckillservice.redis.SeckillSkuBloomFilter;
import com.example.seckillservice.redis.SeckillSkuLock;
import org.redisson.api.RLock;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSeckillStockService implements SeckillStockService {

    @Resource
    private SeckillSkuBloomFilter seckillSkuBloomFilter;

    @Resource
    private SeckillSkuAvailStockCache seckillSkuAvailStockCache;

    @Resource
    private SeckillSkuLock seckillSkuLock;

    @Resource
    private SeckillStockInfoDbService seckillStockInfoDbService;

    @Override
    public SeckillSkuStockResponse getAvailStock(Long skuId, Long activityId) throws BizException {
        SeckillSkuStockResponse response = new SeckillSkuStockResponse()
                .setSkuId(skuId)
                .setActivityId(activityId);

        if (!seckillSkuBloomFilter.contains(skuId, activityId)) {
            return response;
        }

        Integer availStock = seckillSkuAvailStockCache.get(skuId, activityId);
        if (availStock != null) {
            return response.setAvailStock(availStock);
        }

        RLock lock = seckillSkuLock.getLock(skuId, activityId);
        try {
            if (lock != null && lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                availStock = seckillSkuAvailStockCache.get(skuId, activityId);
                if (availStock != null) {
                    return response.setAvailStock(availStock);
                }

                SeckillStockInfo stockInfo = seckillStockInfoDbService.getStockInfo(skuId, activityId);
                if (stockInfo != null) {
                    availStock = stockInfo.getStock() - stockInfo.getLockStock();
                    response.setAvailStock(availStock);
                    seckillSkuAvailStockCache.set(skuId, activityId, availStock, 60 * 10);
                    return response;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return response;
    }
}
