package com.port90.external.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class NuriStockResponse {
    private String basDt;      // 기준 날짜
    private String srtnCd;     // 단축 코드
    private String isinCd;     // ISIN 코드
    private String itmsNm;     // 종목명
    private String mrktCtg;    // 시장 구분
    private String clpr;       // 종가
    private String vs;         // 대비
    private String fltRt;      // 등락률
    private String mkp;        // 시가
    private String hipr;       // 고가
    private String lopr;       // 저가
    private String trqu;       // 거래량
    private String trPrc;      // 거래 금액
    private Long lstgStCnt;  // 상장 주식 수
    private Long mrktTotAmt; // 시가 총액
}
