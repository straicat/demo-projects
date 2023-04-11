package com.example.tinyurl.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BizException extends Exception {
    private int code;
    private String msg;

    public static final BizException GLOBAL_ID_GENERATE_FAIL = new BizException(500001, "全局ID生成失败");
    public static final BizException INVALID_CONFIGURATION = new BizException(500002, "配置错误");
}
