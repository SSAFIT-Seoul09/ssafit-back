package com.ssafy.ssafit.usageTimeTracker.domain.repository;

import com.ssafy.ssafit.usageTimeTracker.domain.model.UsageTimeTracker;
import org.apache.ibatis.annotations.Param;

public interface UsageTimeTrackerDao {
    UsageTimeTracker findByUserId(@Param("userId") long userId);

    void updateTime(@Param("runTime") long runTime);

    void insertTime(UsageTimeTracker usageTimeTracker);
}