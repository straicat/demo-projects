package com.example.seckillservice.service.impl;

import com.example.seckillservice.enums.CacheSyncMethodEnum;
import com.example.seckillservice.service.AbstractSeckillStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillStockServiceAfterDelImpl extends AbstractSeckillStockService {

    @Override
    public Integer getCacheSyncMethod() {
        return CacheSyncMethodEnum.UPDATE_DB_THEN_DELETE_CACHE.getCode();
    }
}
