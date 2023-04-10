package com.example.tinyurl.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BizException extends Exception {
    private int code;
    private String msg;

    public static final BizException GLOBAL_ID_GENERATE_FAIL = new BizException(500001, "全局ID生成失败");
}
