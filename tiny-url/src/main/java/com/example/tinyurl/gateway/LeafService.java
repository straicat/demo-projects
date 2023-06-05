package com.example.tinyurl.gateway;

import com.example.tinyurl.wrapper.HttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class LeafService {
    @Value("${leaf.url:}")
    private String leafUrl;

    @Resource
    private HttpClient httpClient;

    public Long getId() {
        try {
            Response response = httpClient.sendGetRequest(leafUrl);
            if (response.isSuccessful() && response.body() != null) {
                String text = Objects.requireNonNull(response.body()).string();
                return Long.parseLong(text);
            }
        } catch (IOException | NumberFormatException e) {
            log.error("LeafService getId error!", e);
        }
        return null;
    }
}
