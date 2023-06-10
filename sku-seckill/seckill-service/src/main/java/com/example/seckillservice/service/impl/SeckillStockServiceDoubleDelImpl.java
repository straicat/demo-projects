package com.example.seckillservice.service.impl;

import com.example.seckillservice.enums.CacheSyncMethodEnum;
import com.example.seckillservice.service.AbstractSeckillStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SeckillStockServiceDoubleDelImpl extends AbstractSeckillStockService {

    @Override
    public Integer getCacheSyncMethod() {
        return CacheSyncMethodEnum.CACHE_DELAY_DOUBLE_DELETE.getCode();
    }
}
