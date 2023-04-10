package com.example.tinyurl.controller;

import com.example.tinyurl.exception.BizException;
import com.example.tinyurl.model.ApiResult;
import com.example.tinyurl.model.ShortenRequest;
import com.example.tinyurl.model.ShortenResponse;
import com.example.tinyurl.service.TinyUrlService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("")
public class TinyUrlController {

    @Resource
    private TinyUrlService tinyUrlService;

    @GetMapping("/{shortUrl}")
    public void get(@PathVariable("shortUrl") String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = tinyUrlService.getLongUrl(shortUrl);
        if (longUrl != null) {
            response.sendRedirect(longUrl);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PostMapping("/shorten")
    public ApiResult<ShortenResponse> shorten(@RequestBody ShortenRequest request) {
        String longUrl = request.getUrl();
        if (longUrl != null && longUrl.length() > 0) {
            String shortUrl = null;
            try {
                shortUrl = tinyUrlService.createShortUrl(longUrl);
            } catch (BizException e) {
                return new ApiResult<>(500, "ERROR", null);
            }
            ShortenResponse response = new ShortenResponse();
            response.setUrl(shortUrl);
            return ApiResult.success(response);
        }
        return new ApiResult<>(400, "ERROR", null);
    }
}
