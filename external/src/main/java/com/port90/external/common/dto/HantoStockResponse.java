package com.port90.external.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HantoStockResponse {
    @JsonProperty("output")
    private Output output;

    @JsonProperty("rt_cd")
    private String rtCd;

    @JsonProperty("msg_cd")
    private String msgCd;

    @JsonProperty("msg1")
    private String msg1;

    @Getter
    @Setter
    public static class Output {
        private String pdno;                   // 상품번호
        @JsonProperty("prdt_type_cd")
        private String prdtTypeCd;             // 상품유형코드
        @JsonProperty("mket_id_cd")
        private String mketIdCd;               // 시장ID코드
        @JsonProperty("scty_grp_id_cd")
        private String sctyGrpIdCd;            // 증권그룹ID코드
        @JsonProperty("excg_dvsn_cd")
        private String excgDvsnCd;             // 거래소구분코드
        @JsonProperty("setl_mmdd")
        private String setlMmdd;               // 결제월일
        @JsonProperty("lstg_stqt")
        private String lstgStqt;               // 상장주식수
        @JsonProperty("lstg_cptl_amt")
        private String lstgCptlAmt;            // 상장자본금
        private String cpta;                   // 자본금
        private String papr;                   // 액면가
        @JsonProperty("issu_pric")
        private String issuPric;               // 발행가
        @JsonProperty("kospi200_item_yn")
        private String kospi200ItemYn;         // 코스피200편입여부
        @JsonProperty("scts_mket_lstg_dt")
        private String sctsMketLstgDt;         // 증권시장상장일자
        @JsonProperty("scts_mket_lstg_abol_dt")
        private String sctsMketLstgAbolDt;     // 증권시장상장폐지일자
        @JsonProperty("kosdaq_mket_lstg_dt")
        private String kosdaqMketLstgDt;       // 코스닥시장상장일자
        @JsonProperty("kosdaq_mket_lstg_abol_dt")
        private String kosdaqMketLstgAbolDt;   // 코스닥시장상장폐지일자
        @JsonProperty("frbd_mket_lstg_dt")
        private String frbdMketLstgDt;         // 프리보드시장상장일자
        @JsonProperty("frbd_mket_lstg_abol_dt")
        private String frbdMketLstgAbolDt;     // 프리보드시장상장폐지일자
        @JsonProperty("reits_kind_cd")
        private String reitsKindCd;            // 리츠종류코드
        @JsonProperty("etf_dvsn_cd")
        private String etfDvsnCd;              // ETF구분코드
        @JsonProperty("oilf_fund_yn")
        private String oilfFundYn;             // 원유펀드여부
        @JsonProperty("idx_bztp_lcls_cd")
        private String idxBztpLclsCd;          // 산업대분류코드
        @JsonProperty("idx_bztp_mcls_cd")
        private String idxBztpMclsCd;          // 산업중분류코드
        @JsonProperty("idx_bztp_scls_cd")
        private String idxBztpSclsCd;          // 산업소분류코드
        @JsonProperty("stck_kind_cd")
        private String stckKindCd;             // 주식종류코드
        @JsonProperty("mfnd_opng_dt")
        private String mfndOpngDt;             // 펀드개시일자
        @JsonProperty("mfnd_end_dt")
        private String mfndEndDt;              // 펀드종료일자
        @JsonProperty("dpsi_erlm_cncl_dt")
        private String dpsiErlmCnclDt;         // 배당금지급일취소일자
        @JsonProperty("etf_cu_qty")
        private String etfCuQty;               // ETF CU 수량
        @JsonProperty("prdt_name")
        private String prdtName;               // 상품명
        @JsonProperty("prdt_name120")
        private String prdtName120;            // 상품명(120)
        @JsonProperty("prdt_abrv_name")
        private String prdtAbrvName;           // 상품약어명
        @JsonProperty("std_pdno")
        private String stdPdno;                // 표준상품번호
        @JsonProperty("prdt_eng_name")
        private String prdtEngName;            // 상품영문명
        @JsonProperty("prdt_eng_name120")
        private String prdtEngName120;         // 상품영문명(120)
        @JsonProperty("prdt_eng_abrv_name")
        private String prdtEngAbrvName;        // 상품영문약어명
        @JsonProperty("dpsi_aptm_erlm_yn")
        private String dpsiAptmErlmYn;         // 배당정산여부
        @JsonProperty("etf_txtn_type_cd")
        private String etfTxtnTypeCd;          // ETF 거래 유형 코드
        @JsonProperty("etf_type_cd")
        private String etfTypeCd;              // ETF 종류 코드
        @JsonProperty("lstg_abol_dt")
        private String lstgAbolDt;             // 상장폐지일자
        @JsonProperty("nwst_odst_dvsn_cd")
        private String nwstOdstDvsnCd;         // 공시조회구분코드
        @JsonProperty("sbst_pric")
        private String sbstPric;               // 기준가
        @JsonProperty("thco_sbst_pric")
        private String thcoSbstPric;           // 기준가
        @JsonProperty("thco_sbst_pric_chng_dt")
        private String thcoSbstPricChngDt;     // 기준가변경일자
        @JsonProperty("tr_stop_yn")
        private String trStopYn;               // 거래정지여부
        @JsonProperty("admn_item_yn")
        private String admnItemYn;             // 관리종목여부
        @JsonProperty("thdt_clpr")
        private String thdtClpr;               // 당일 종가
        @JsonProperty("bfdy_clpr")
        private String bfdyClpr;               // 전일 종가
        @JsonProperty("clpr_chng_dt")
        private String clprChngDt;             // 종가 변경일자
        @JsonProperty("std_idst_clsf_cd")
        private String stdIdstClsfCd;          // 표준산업분류코드
        @JsonProperty("std_idst_clsf_cd_name")
        private String stdIdstClsfCdName;      // 표준산업분류명
        @JsonProperty("idx_bztp_lcls_cd_name")
        private String idxBztpLclsCdName;      // 산업대분류명
        @JsonProperty("idx_bztp_mcls_cd_name")
        private String idxBztpMclsCdName;      // 산업중분류명
        @JsonProperty("idx_bztp_scls_cd_name")
        private String idxBztpSclsCdName;      // 산업소분류명
        @JsonProperty("ocr_no")
        private String ocrNo;                  // OCR 번호
        @JsonProperty("crfd_item_yn")
        private String crfdItemYn;             // 증권사항목여부
        @JsonProperty("elec_scty_yn")
        private String elecSctyYn;             // 전자증권여부
    }
}
