package com.example.tinyurl.wrapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class HttpClientService {
    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    public Response sendGetRequest(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (headers != null) {
            for (Map.Entry<String, String> kv : headers.entrySet()) {
                builder.addHeader(kv.getKey(), kv.getValue());
            }
        }
        Request request = builder.build();
        return client.newCall(request).execute();
    }

    public Response sendGetRequest(String url) throws IOException {
        return sendGetRequest(url, null);
    }
}
