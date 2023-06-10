package com.example.seckillservice.exception;

import com.example.seckillservice.enums.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BizException extends Exception {
    private int code;
    private String msg;

    public BizException(ErrorCode errorCode) {
        code = errorCode.getCode();
        msg = errorCode.getDesc();
    }
}
