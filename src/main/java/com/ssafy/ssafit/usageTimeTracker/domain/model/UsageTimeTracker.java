package com.ssafy.ssafit.usageTimeTracker.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageTimeTracker extends TimeStamped {

    private Long id;
    private Long userId;
    private Long totalTime;

    public static UsageTimeTracker of(Long userId, long runtime) {
        return UsageTimeTracker.builder()
                .userId(userId)
                .totalTime(runtime)
                .build();

    }

    public void addRunTime(long runtime) {
        totalTime += runtime;
    }
}