package com.port90.core.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class MarketCapResponse {
    private String rt_cd;
    private String msg_cd; //응답코드
    private String msg1;
    private List<MarketCap> output; // 거래량 순위 리스트
}
