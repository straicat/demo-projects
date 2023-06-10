package com.example.orderservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrderRequest {
    private Long skuId;
    private Long activityId;
    private Long userId;
    private Integer buyCnt;
}
