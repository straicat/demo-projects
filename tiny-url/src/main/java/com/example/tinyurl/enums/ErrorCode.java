package com.example.tinyurl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    GLOBAL_ID_GENERATE_FAIL(500001, "全局ID生成失败"),
    INVALID_CONFIGURATION(500002, "配置错误"),
    GLOBAL_ID_OVERFLOW(500003, "全局ID溢出");
    ;

    private final Integer code;
    private final String desc;
}
