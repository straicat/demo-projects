package com.example.seckillservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SeckillSkuStockResponse {
    private Long skuId;
    private Long activityId;
    private Integer availStock;
}
