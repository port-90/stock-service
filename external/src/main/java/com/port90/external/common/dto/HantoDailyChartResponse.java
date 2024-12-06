package com.port90.external.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HantoDailyChartResponse {

    @JsonProperty("rt_cd")
    private String rtCode; // 성공 실패 여부 (0: 성공)

    @JsonProperty("msg_cd")
    private String msgCode; // 응답 코드

    @JsonProperty("msg1")
    private String message; // 응답 메시지

    @JsonProperty("output1")
    private HantoDailyChartCurrent output1; // 응답 상세 1

    @JsonProperty("output2")
    private List<HantoDailyChartRow> output2; // 일별 데이터 배열

}
