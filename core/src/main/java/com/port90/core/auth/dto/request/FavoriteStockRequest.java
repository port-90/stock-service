package com.port90.core.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteStockRequest {
    private String stockCode;
    private String stockName;
}
