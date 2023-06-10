package com.example.orderservice.service;

import com.alibaba.fastjson2.JSONObject;
import com.example.orderservice.enums.PayStatusEnum;
import com.example.orderservice.mapper.SeckillOrderInfoMapper;
import com.example.orderservice.model.CreateOrderRequest;
import com.example.orderservice.model.SeckillOrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class SeckillOrderInfoDbService {

    @Resource
    private SeckillOrderInfoMapper seckillOrderInfoMapper;

    public SeckillOrderInfo createOrder(CreateOrderRequest request) {
        SeckillOrderInfo info = new SeckillOrderInfo();
        info.setSkuId(request.getSkuId());
        info.setActivityId(request.getActivityId());
        info.setUserId(request.getUserId());
        info.setBuyCnt(request.getBuyCnt());
        info.setPayStatus(PayStatusEnum.UNPAID.getCode());
        seckillOrderInfoMapper.insert(info);
        log.info("SeckillOrderInfoDbService.createOrder: {}", JSONObject.toJSONString(info));
        return info;
    }
}
