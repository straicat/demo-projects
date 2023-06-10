package com.example.seckillservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(500000, "内部错误"),
    NO_SERVICE_FOUND(500001, "找不到服务"),
    ;

    private final Integer code;
    private final String desc;
}
