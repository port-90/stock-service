package com.port90.core.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FavoriteStockResponse {
    private String stockCode;
    private String stockName;
    private LocalDateTime createdAt;
}
