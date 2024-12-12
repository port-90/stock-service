package com.port90.external.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class RateRankResponse {

    @JsonProperty("rt_cd")
    private String rtCd;

    @JsonProperty("msg_cd")
    private String msgCd; // 응답코드

    @JsonProperty("msg1")
    private String msg1;

    @JsonProperty("output")
    private List<RateRank> output; // 상승률/하락률 순위 리스트

    @Getter
    @Setter
    @ToString
    public static class RateRank {
        @JsonProperty("hts_kor_isnm")
        private String htsKorIsnm; // 종목명

        @JsonProperty("mksc_shrn_iscd")
        private String mkscShrnIscd; // 종목 코드

        @JsonProperty("data_rank")
        private String dataRank; // 데이터 순위

        @JsonProperty("stck_prpr")
        private String stckPrpr; // 현재가

        @JsonProperty("prdy_vrss_sign")
        private String prdyVrssSign; // 전일 대비 부호

        @JsonProperty("prdy_vrss")
        private String prdyVrss; // 전일 대비

        @JsonProperty("prdy_ctrt")
        private String prdyCtrt; // 전일 대비율

        @JsonProperty("acml_vol")
        private String acmlVol; // 누적 거래량
    }
}
