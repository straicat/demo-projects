package com.example.seckillservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheSyncMethodEnum {
    UPDATE_DB_THEN_DELETE_CACHE(1, "更新数据库，然后删除缓存"),
    CACHE_DELAY_DOUBLE_DELETE(2, "缓存延迟双删：先删除缓存，再更新数据库，然后再次删除缓存"),
    CANAL_LISTEN_BINLOG(3, "基于Canal监听MySQL的Binlog实现缓存同步"),
    ;

    private int code;
    private String desc;
}
