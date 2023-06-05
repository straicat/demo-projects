package com.example.tinyurl.service;

import com.example.tinyurl.exception.BizException;

public interface ShortUrlCreator {

    String getShortUrl(String longUrl) throws BizException;

    Integer getShortenMethod();
}
