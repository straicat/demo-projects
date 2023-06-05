package com.example.tinyurl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ShortUrlCreatorFactory {

    private Map<Integer, ShortUrlCreator> shortUrlCreatorMap;

    @Resource
    private List<ShortUrlCreator> shortUrlCreatorList;

    @Value("${shorten.method}")
    private Integer shortenMethod;

    @PostConstruct
    void init() {
        shortUrlCreatorMap = shortUrlCreatorList.stream()
                .collect(Collectors.toMap(ShortUrlCreator::getShortenMethod, Function.identity()));
    }

    public ShortUrlCreator getShortUrlCreator() {
        return shortUrlCreatorMap.get(shortenMethod);
    }
}
