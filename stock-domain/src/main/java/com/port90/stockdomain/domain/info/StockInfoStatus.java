package com.port90.stockdomain.domain.info;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StockInfoStatus {
    OPEN("거래중"),
    CLOSE("상장폐지"),
    STOP("거래정지");
    private final String discription;
}
