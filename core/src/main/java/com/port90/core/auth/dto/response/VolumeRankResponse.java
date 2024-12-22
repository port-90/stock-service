package com.port90.core.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class VolumeRankResponse {
    @JsonProperty("rt_cd")
    private String rtCd;

    @JsonProperty("msg_cd")
    private String msgCd; // 응답코드

    @JsonProperty("msg1")
    private String msg1;

    @JsonProperty("output")
    private List<VolumeRank> output;// 거래량 순위 리스트
}

