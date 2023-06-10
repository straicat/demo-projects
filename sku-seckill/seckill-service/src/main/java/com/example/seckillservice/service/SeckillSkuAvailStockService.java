package com.example.seckillservice.service;

import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.SeckillSkuStockResponse;

public interface SeckillSkuAvailStockService {
    /**
     * 查询可用库存
     * @param skuId
     * @param activityId
     * @return
     * @throws BizException
     */
    SeckillSkuStockResponse getAvailStock(Long skuId, Long activityId) throws BizException;

    Integer getCacheSyncMethod();
}
