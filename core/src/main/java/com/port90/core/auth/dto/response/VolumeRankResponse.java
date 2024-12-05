package com.port90.core.auth.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class VolumeRankResponse {
    private String rt_cd;
    private String msg_cd; //응답코드
    private String msg1;
    private List<VolumeRank> output; // 거래량 순위 리스트
}

