package com.port90.stockdomain.domain.info;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StockStatus {
    STOP("거래정지"),
    CLOSE("상장폐지"),
    OPEN("거래가능");
    private final String discription;
}
