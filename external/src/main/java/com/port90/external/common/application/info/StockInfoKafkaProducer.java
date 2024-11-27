package com.port90.external.common.application.info;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StockInfoKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final static String TOPIC_NAME = "stock_code";
    private final static int PARTITION_SIZE = 4;

    public void sendStockCode(List<String> messages) {
        int p = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (i % 15 == 0) {
                p++;
                p = p % PARTITION_SIZE;
            }
            String partitionKey = String.format("partition-%d", i);
            kafkaTemplate.send(TOPIC_NAME, p, partitionKey, messages.get(i));
        }
    }
}
