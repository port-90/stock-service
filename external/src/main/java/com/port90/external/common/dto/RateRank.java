package com.port90.external.common.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class RateRank {

    @JsonProperty("mksc_shrn_iscd")
    private String mkscShrnIscd; // 종목 코드

    @JsonProperty("data_rank")
    private String dataRank; // 데이터 순위

    @JsonProperty("hts_kor_isnm")
    private String htsKorIsnm; // 종목명

    @JsonProperty("stck_prpr")
    private String stckPrpr; // 현재가

    @JsonProperty("prdy_vrss")
    private String prdyVrss; // 전일 대비

    @JsonProperty("prdy_vrss_sign")
    private String prdyVrssSign; // 전일 대비 부호

    @JsonProperty("prdy_ctrt")
    private String prdyCtrt; // 전일 대비율
}
