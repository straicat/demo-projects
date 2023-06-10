package com.example.seckillservice.service.impl;

import com.example.seckillservice.enums.CacheSyncMethodEnum;
import com.example.seckillservice.model.SeckillOrderRequest;
import com.example.seckillservice.model.SeckillOrderResponse;
import com.example.seckillservice.service.SeckillOrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SeckillOrderServiceAfterDelImpl implements SeckillOrderService {

    @Override
    public SeckillOrderResponse createOrder(SeckillOrderRequest request) {

        // 先锁定库存，创建订单

        // 再删除缓存

        // 如果缓存删除失败，放到MQ进行删除

        return null;
    }

    @Override
    public List<Integer> cacheSyncMethods() {
        return Lists.newArrayList(CacheSyncMethodEnum.UPDATE_DB_THEN_DELETE_CACHE.getCode());
    }
}
