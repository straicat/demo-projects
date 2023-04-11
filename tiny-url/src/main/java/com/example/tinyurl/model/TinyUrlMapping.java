package com.example.tinyurl.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TinyUrlMapping {
    // 这里需要添加这个注解，用于标识自增字段
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String shortUrl;
    private String longUrl;
    private Long ctime;
}
