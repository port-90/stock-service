package com.port90.core.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketCap {
    @JsonProperty("mksc_shrn_iscd")
    private String mkscShrnIscd;  // 종목 코드

    @JsonProperty("data_rank")
    private String dataRank;      // 순위

    @JsonProperty("hts_kor_isnm")
    private String htsKorIsnm;    // 종목명

    @JsonProperty("stck_prpr")
    private String stckPrpr;      // 현재가

    @JsonProperty("prdy_vrss")
    private String prdyVrss;      // 전일 대비

    @JsonProperty("prdy_vrss_sign")
    private String prdyVrssSign;  // 전일 대비 부호

    @JsonProperty("prdy_ctrt")
    private String prdyCtrt;      // 전일 대비율

    @JsonProperty("acml_vol")
    private String acmlVol;       // 누적 거래량

    @JsonProperty("lstn_stcn")
    private String lstnStcn;      // 상장 주수

    @JsonProperty("stck_avls")
    private String stckAvls;      // 시가총액
}