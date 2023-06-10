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
public class SeckillSkuAvailStockServiceFactory {

    private Map<Integer, SeckillSkuAvailStockService> seckillSkuAvailStockServiceMap;

    @Resource
    private List<SeckillSkuAvailStockService> seckillSkuAvailStockServiceList;

    @Value("${cacheSyncMethod}")
    private Integer cacheSyncMethod;

    @PostConstruct
    void init() {
        seckillSkuAvailStockServiceMap = seckillSkuAvailStockServiceList.stream()
                .collect(Collectors.toMap(SeckillSkuAvailStockService::getCacheSyncMethod, Function.identity()));
    }

    public SeckillSkuAvailStockService getService() {
        return seckillSkuAvailStockServiceMap.get(cacheSyncMethod);
    }
}
