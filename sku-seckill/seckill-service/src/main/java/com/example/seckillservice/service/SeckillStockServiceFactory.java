package com.example.seckillservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SeckillStockServiceFactory {

    private Map<Integer, SeckillStockService> seckillSkuAvailStockServiceMap;

    @Resource
    private List<SeckillStockService> seckillStockServiceList;

    @Value("${cacheSyncMethod}")
    private Integer cacheSyncMethod;

    @PostConstruct
    void init() {
        seckillSkuAvailStockServiceMap = seckillStockServiceList.stream()
                .collect(Collectors.toMap(SeckillStockService::getCacheSyncMethod, Function.identity()));
    }

    public SeckillStockService getService() {
        return seckillSkuAvailStockServiceMap.get(cacheSyncMethod);
    }
}
