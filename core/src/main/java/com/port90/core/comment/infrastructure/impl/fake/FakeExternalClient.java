package com.port90.core.comment.infrastructure.impl.fake;

import com.port90.core.comment.infrastructure.ExternalClient;
import org.springframework.stereotype.Component;

@Component
public class FakeExternalClient implements ExternalClient {
    @Override
    public boolean existsByStockCode(String stockCode) {
        return true;
    }
}
