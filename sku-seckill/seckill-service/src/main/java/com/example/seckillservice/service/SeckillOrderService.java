package com.example.seckillservice.service;

import com.example.seckillservice.model.SeckillOrderRequest;
import com.example.seckillservice.model.SeckillOrderResponse;

import java.util.List;

public interface SeckillOrderService {
    SeckillOrderResponse createOrder(SeckillOrderRequest request);

    List<Integer> cacheSyncMethods();
}
