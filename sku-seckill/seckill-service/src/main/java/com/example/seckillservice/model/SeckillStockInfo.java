package com.example.seckillservice.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SeckillStockInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private Long activityId;
    private Integer stock;
    private Integer lockStock;
}
