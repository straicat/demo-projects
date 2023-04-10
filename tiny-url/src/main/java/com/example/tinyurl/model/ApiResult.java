package com.example.tinyurl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResult<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "SUCCESS", data);
    }
}
