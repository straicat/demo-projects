package com.example.seckillservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SeckillOrderResponse {
    private Boolean orderSuccess;
    private Long orderId;
    private Integer remainStock;
    private Long skuId;
    private Long activityId;
    private Long userId;
    private Integer buyCnt;
}
