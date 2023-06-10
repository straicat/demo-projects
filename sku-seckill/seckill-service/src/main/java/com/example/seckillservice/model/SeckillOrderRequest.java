package com.example.seckillservice.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SeckillOrderRequest {
    private Long skuId;
    private Long activityId;
    private Long userId;
    private Integer buyCnt = 1;
    private JSONObject otherInfo;
}
