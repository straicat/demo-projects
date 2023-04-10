package com.example.tinyurl.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class TinyUrlMapping {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String shortUrl;
    private String longUrl;
    private Long ctime;
}
