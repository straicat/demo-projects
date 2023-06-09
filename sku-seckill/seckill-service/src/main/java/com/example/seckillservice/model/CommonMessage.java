package com.example.seckillservice.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonMessage {
    private String database;                               // 数据库或schema
    private String table;                                  // 表名
    private List<String> pkNames;
    private Boolean isDdl;
    // 类型:INSERT/UPDATE/DELETE
    private String type;
    // binlog executeTime, 执行耗时
    private Long es;
    // dml build timeStamp, 同步时间
    private Long ts;
    // 执行的sql,dml sql为空
    private String sql;
    // 数据列表
    private List<Map<String, Object>> data;
    // 旧数据列表,用于update,size和data的size一一对应
    private List<Map<String, Object>> old;
}
