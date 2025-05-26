package com.ssafy.ssafit.usageTimeTracker.dto;

import lombok.Getter;

@Getter
public class RankResponseDto {

    private long id;
    private long userId;
    private long totalTime;
    private String nickname;
}
