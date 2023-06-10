package com.example.seckillservice.service;

import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.SeckillSkuStockResponse;

public interface SeckillStockService {
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
