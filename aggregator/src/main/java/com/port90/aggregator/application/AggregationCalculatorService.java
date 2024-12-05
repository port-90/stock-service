package com.port90.aggregator.application;

import com.port90.aggregator.dto.AggregatedResult;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class AggregationCalculatorService {

    public <T> AggregatedResult calculateAggregatedData(
            List<T> data,
            Function<T, String> highPriceExtractor,
            Function<T, String> lowPriceExtractor,
            Function<T, String> volumeExtractor,
            Function<T, String> valueExtractor
    ) {
        long highPrice = 0L;
        long lowPrice = Long.MAX_VALUE;
        long totalVolume = 0L;
        long totalValue = 0L;

        for (T item : data) {
            highPrice = Math.max(highPrice, Long.parseLong(highPriceExtractor.apply(item)));
            lowPrice = Math.min(lowPrice, Long.parseLong(lowPriceExtractor.apply(item)));
            totalVolume += Long.parseLong(volumeExtractor.apply(item));
            totalValue += Long.parseLong(valueExtractor.apply(item));
        }

        return new AggregatedResult(
                String.valueOf(highPrice),
                String.valueOf(lowPrice),
                String.valueOf(totalVolume),
                String.valueOf(totalValue)
        );
    }
}
