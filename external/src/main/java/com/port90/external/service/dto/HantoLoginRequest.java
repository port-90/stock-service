package com.port90.external.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HantoLoginRequest {
    @JsonProperty("grant_type")
    private String grantType = "client_credentials";

    @JsonProperty("appsecret")
    private String appSecret;

    @JsonProperty("appkey")
    private String appKey;

    public HantoLoginRequest(String appSecret, String appKey) {
        this.appSecret = appSecret;
        this.appKey = appKey;
    }
}
