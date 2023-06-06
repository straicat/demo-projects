package com.example.tinyurl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShortenMethodEnum {
    GLOBAL_ID(1, "基于全局ID"),
    HASH(2, "基于Hash"),
    ;

    private int code;
    private String desc;
}
