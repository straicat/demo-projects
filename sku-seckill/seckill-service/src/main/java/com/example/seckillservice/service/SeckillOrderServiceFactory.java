package com.example.seckillservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SeckillOrderServiceFactory {

    private Map<Integer, SeckillOrderService> seckillOrderServiceMap;

    @Resource
    private List<SeckillOrderService> seckillOrderServiceList;

    @Value("${cacheSyncMethod}")
    private Integer cacheSyncMethod;

    @PostConstruct
    void init() {
        seckillOrderServiceMap = new ConcurrentHashMap<>();
        for (SeckillOrderService service : seckillOrderServiceList) {
            for (Integer method : service.cacheSyncMethods()) {
                seckillOrderServiceMap.put(method, service);
            }
        }
    }

    public SeckillOrderService getService() {
        return seckillOrderServiceMap.get(cacheSyncMethod);
    }
}
