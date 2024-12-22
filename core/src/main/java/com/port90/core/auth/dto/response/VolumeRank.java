package com.port90.core.auth.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class VolumeRank {
    private String hts_kor_isnm; // 종목명
    private String mksc_shrn_iscd; // 단축 종목 코드
    private String data_rank; // 데이터 순위
    private String stck_prpr; // 현재가
    private String prdy_vrss_sign; // 전일 대비 부호
    private String prdy_vrss; // 전일 대비
    private String prdy_ctrt; // 전일 대비율
    private String acml_vol; // 누적 거래량
}

