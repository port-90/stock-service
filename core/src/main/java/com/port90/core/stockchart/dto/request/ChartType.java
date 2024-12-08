package com.port90.core.stockchart.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChartType {
    MINUTE("minute"),
    HOURLY("hourly"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    private final String value;
}
