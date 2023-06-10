package com.example.seckillservice.service.impl;

import com.example.seckillservice.model.SeckillOrderRequest;
import com.example.seckillservice.model.SeckillOrderResponse;
import com.example.seckillservice.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SeckillOrderServiceSyncImpl implements SeckillOrderService {
    @Override
    public SeckillOrderResponse createOrder(SeckillOrderRequest request) {
        return null;
    }

    @Override
    public List<Integer> cacheSyncMethods() {
        return null;
    }
}
