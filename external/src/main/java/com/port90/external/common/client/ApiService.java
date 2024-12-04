package com.port90.external.common.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiService {
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

    public ResponseEntity<String> getForSimpleJson(URI uri) {
        log.info("[GET] Request to URL: {}", uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }
}
