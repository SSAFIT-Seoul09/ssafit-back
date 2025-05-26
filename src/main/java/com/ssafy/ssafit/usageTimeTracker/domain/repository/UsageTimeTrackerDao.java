package com.ssafy.ssafit.usageTimeTracker.domain.repository;

import com.ssafy.ssafit.usageTimeTracker.domain.model.UsageTimeTracker;
import com.ssafy.ssafit.usageTimeTracker.dto.RankResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsageTimeTrackerDao {
    UsageTimeTracker findByUserId(@Param("userId") long userId);

    void updateTime(@Param("runTime") long runTime);

    void insertTime(UsageTimeTracker usageTimeTracker);

    List<RankResponseDto> getRankTopTen();
}