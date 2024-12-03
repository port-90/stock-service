package com.port90.external.common.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class HantoApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public <T> T postForObject(String url, Object request, Class<T> responseType) {
        log.info("[POST] Request to URL: {}", url);
        return restTemplate.postForObject(url, request, responseType);
    }

    public <T> ResponseEntity<T> getForObject(String url, HttpHeaders headers, Class<T> responseType) {
        log.info("[GET] Request to URL: {}", url);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }
}
