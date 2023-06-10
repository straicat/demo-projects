package com.example.orderservice.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SeckillOrderInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private Long activityId;
    private Long userId;
    private Integer buyCnt;
    private Integer payStatus;
}
