package com.port90.external.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HantoDailyChartCurrent {

    @JsonProperty("prdy_vrss")
    private String previousDayDifference; // 전일 대비

    @JsonProperty("prdy_vrss_sign")
    private String previousDayDifferenceSign; // 전일 대비 부호

    @JsonProperty("prdy_ctrt")
    private String previousDayRate; // 전일 대비율

    @JsonProperty("stck_prdy_clpr")
    private String stockPreviousClosePrice; // 주식 전일 종가

    @JsonProperty("acml_vol")
    private String accumulatedVolume; // 누적 거래량

    @JsonProperty("acml_tr_pbmn")
    private String accumulatedTransactionAmount; // 누적 거래 대금

    @JsonProperty("hts_kor_isnm")
    private String htsKoreanStockName; // HTS 한글 종목명

    @JsonProperty("stck_prpr")
    private String currentStockPrice; // 주식 현재가

    @JsonProperty("stck_shrn_iscd")
    private String stockShortCode; // 주식 단축 종목코드

    @JsonProperty("prdy_vol")
    private String previousDayVolume; // 전일 거래량

    @JsonProperty("stck_mxpr")
    private String upperLimitPrice; // 상한가

    @JsonProperty("stck_llam")
    private String lowerLimitPrice; // 하한가

    @JsonProperty("stck_oprc")
    private String openingPrice; // 시가

    @JsonProperty("stck_hgpr")
    private String highPrice; // 최고가

    @JsonProperty("stck_lwpr")
    private String lowPrice; // 최저가

    @JsonProperty("stck_prdy_oprc")
    private String previousDayOpeningPrice; // 주식 전일 시가

    @JsonProperty("stck_prdy_hgpr")
    private String previousDayHighPrice; // 주식 전일 최고가

    @JsonProperty("stck_prdy_lwpr")
    private String previousDayLowPrice; // 주식 전일 최저가

    @JsonProperty("askp")
    private String askPrice; // 매도호가

    @JsonProperty("bidp")
    private String bidPrice; // 매수호가

    @JsonProperty("prdy_vrss_vol")
    private String previousDayVolumeDifference; // 전일 대비 거래량

    @JsonProperty("vol_tnrt")
    private String volumeTurnoverRate; // 거래량 회전율

    @JsonProperty("stck_fcam")
    private String stockFaceValue; // 주식 액면가

    @JsonProperty("lstn_stcn")
    private String listedShares; // 상장 주수

    @JsonProperty("cpfn")
    private String capital; // 자본금

    @JsonProperty("hts_avls")
    private String marketCap; // 시가총액

    @JsonProperty("per")
    private String per; // PER

    @JsonProperty("eps")
    private String eps; // EPS

    @JsonProperty("pbr")
    private String pbr; // PBR

    @JsonProperty("itewhol_loan_rmnd_ratem")
    private String wholeLoanRemainRate; // 전체 융자 잔고 비율
}
