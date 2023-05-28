package com.example.tinyurl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ShortUrlGenMethodEnum {
    GLOBAL_ID(1, "基于全局ID"),
    HASH(2, "基于Hash"),
    ;

    @Getter
    private int code;
    @Getter
    private String desc;
}
