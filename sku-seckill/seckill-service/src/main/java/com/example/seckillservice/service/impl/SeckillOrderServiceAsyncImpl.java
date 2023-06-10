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
public class SeckillOrderServiceAsyncImpl implements SeckillOrderService {
    @Override
    public SeckillOrderResponse createOrder(SeckillOrderRequest request) {
        return null;
    }

    @Override
    public List<Integer> cacheSyncMethods() {
        return Lists.newArrayList(CacheSyncMethodEnum.ASYNC_ORDER_BY_MQ.getCode());
    }
}
