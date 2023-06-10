package com.example.seckillservice.mq;

import com.alibaba.fastjson2.JSON;
import com.example.seckillservice.enums.CacheSyncMethodEnum;
import com.example.seckillservice.model.CommonMessage;
import com.example.seckillservice.redis.SeckillSkuAvailStockCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Slf4j
public class CanalKafkaConsumer {

    private static final String SKU_STOCK_DB = "demo_projects";
    private static final String SKU_STOCK_TABLE = "seckill_stock_info";

    @Resource
    private SeckillSkuAvailStockCache seckillSkuAvailStockCache;

    @Value("${cacheSyncMethod}")
    private Integer cacheSyncMethod;

    @KafkaListener(topics = "canal.seckill_stock_info", groupId = "canal.seckill_stock_info.cache_sync")
    public void listen(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack) {
        if (cacheSyncMethod != CacheSyncMethodEnum.CANAL_LISTEN_BINLOG.getCode()) {
            ack.acknowledge();
            return;
        }

        CommonMessage message = JSON.parseObject(consumerRecord.value().toString(), CommonMessage.class);
        if (message == null || !SKU_STOCK_DB.equals(message.getDatabase()) || !SKU_STOCK_TABLE.equals(message.getTable())) {
            ack.acknowledge();
            return;
        }

        String type = message.getType();
        for (Map<String, Object> datum : message.getData()) {
            Long skuId = Long.parseLong(datum.get("sku_id").toString());
            Long activityId = Long.parseLong(datum.get("activity_id").toString());

            if ("DELETE".equals(type)) {
                seckillSkuAvailStockCache.delete(skuId, activityId);
            } else if ("UPDATE".equals(type) || "INSERT".equals(type)) {
                Integer stock = Integer.parseInt(datum.get("stock").toString());
                Integer lockStock = Integer.parseInt(datum.get("lock_stock").toString());
                Integer availStock = stock - lockStock;
                seckillSkuAvailStockCache.set(skuId, activityId, availStock);
            }
        }
        ack.acknowledge();
    }
}
