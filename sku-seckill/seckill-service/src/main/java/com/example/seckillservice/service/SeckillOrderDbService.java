package com.example.seckillservice.service;

import com.example.seckillservice.model.SeckillOrderRequest;
import com.example.seckillservice.model.SeckillOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillOrderDbService {

    public SeckillOrderResponse createOrderByPessimisticLock(SeckillOrderRequest request) {
        SeckillOrderResponse response = new SeckillOrderResponse();
        // 悲观锁 select for update

        // 锁库存

        // 创建订单

        // 分布式事务
        return response;
    }
}
