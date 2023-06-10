package com.example.seckillservice.service;

import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.SeckillSkuStockResponse;

import java.util.List;

public interface SeckillStockService {
    /**
     * 查询可用库存
     * @param skuId
     * @param activityId
     * @return
     * @throws BizException
     */
    SeckillSkuStockResponse getAvailStock(Long skuId, Long activityId) throws BizException;

    List<Integer> cacheSyncMethods();
}
