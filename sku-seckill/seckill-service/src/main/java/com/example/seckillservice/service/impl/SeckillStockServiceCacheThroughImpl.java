package com.example.seckillservice.service.impl;

import com.example.seckillservice.enums.CacheSyncMethodEnum;
import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.SeckillSkuStockResponse;
import com.example.seckillservice.redis.SeckillSkuAvailStockCache;
import com.example.seckillservice.service.SeckillStockService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SeckillStockServiceCacheThroughImpl implements SeckillStockService {

    @Resource
    private SeckillSkuAvailStockCache seckillSkuAvailStockCache;

    @Override
    public SeckillSkuStockResponse getAvailStock(Long skuId, Long activityId) throws BizException {
        Integer availStock = seckillSkuAvailStockCache.get(skuId, activityId);
        return new SeckillSkuStockResponse()
                .setSkuId(skuId)
                .setActivityId(activityId)
                .setAvailStock(availStock);
    }

    @Override
    public List<Integer> cacheSyncMethods() {
        return Lists.newArrayList(CacheSyncMethodEnum.CANAL_LISTEN_BINLOG.getCode(),
                CacheSyncMethodEnum.ASYNC_ORDER_BY_MQ.getCode());
    }
}
