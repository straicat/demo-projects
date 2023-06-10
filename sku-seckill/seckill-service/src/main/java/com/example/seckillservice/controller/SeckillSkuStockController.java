package com.example.seckillservice.controller;

import com.example.seckillservice.enums.ErrorCode;
import com.example.seckillservice.exception.BizException;
import com.example.seckillservice.model.ApiResult;
import com.example.seckillservice.model.SeckillSkuStockResponse;
import com.example.seckillservice.service.SeckillStockService;
import com.example.seckillservice.service.SeckillStockServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/seckillSku")
public class SeckillSkuStockController {

    @Resource
    private SeckillStockServiceFactory seckillStockServiceFactory;

    @GetMapping("/getAvailStock")
    public ApiResult<SeckillSkuStockResponse> getAvailStock(@RequestParam("skuId") Long skuId, @RequestParam("activityId") Long activityId) {
        SeckillStockService service = seckillStockServiceFactory.getService();
        if (service == null) {
            return new ApiResult<>(ErrorCode.NO_SERVICE_FOUND.getCode(), ErrorCode.NO_SERVICE_FOUND.getDesc(), null);
        }

        try {
            return ApiResult.success(service.getAvailStock(skuId, activityId));
        } catch (BizException e) {
            log.error("getAvailStock error! skuId={}, activityId={}", skuId, activityId, e);
            return new ApiResult<>(ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage(), null);
        }
    }
}
