package com.port90.external.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HantoDailyChartRow {

    @JsonProperty("stck_bsop_date")
    private String businessDate; // 주식 영업 일자

    @JsonProperty("stck_clpr")
    private String closingPrice; // 주식 종가

    @JsonProperty("stck_oprc")
    private String openingPrice; // 주식 시가

    @JsonProperty("stck_hgpr")
    private String highPrice; // 주식 최고가

    @JsonProperty("stck_lwpr")
    private String lowPrice; // 주식 최저가

    @JsonProperty("acml_vol")
    private String accumulatedVolume; // 누적 거래량

    @JsonProperty("acml_tr_pbmn")
    private String accumulatedTransactionAmount; // 누적 거래 대금

    @JsonProperty("flng_cls_code")
    private String floatingClassCode; // 락 구분 코드

    @JsonProperty("prtt_rate")
    private String partitionRate; // 분할 비율

    @JsonProperty("mod_yn")
    private String modificationFlag; // 분할 변경 여부 (Y/N)

    @JsonProperty("prdy_vrss_sign")
    private String previousDayDifferenceSign; // 전일 대비 부호

    @JsonProperty("prdy_vrss")
    private String previousDayDifference; // 전일 대비

    @JsonProperty("revl_issu_reas")
    private String reevaluationReasonCode; // 재평가 사유 코드
}
