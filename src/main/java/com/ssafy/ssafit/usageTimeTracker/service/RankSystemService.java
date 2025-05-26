package com.ssafy.ssafit.usageTimeTracker.service;

import com.ssafy.ssafit.usageTimeTracker.dto.RankResponseDto;

import java.util.List;

public interface RankSystemService {

    List<RankResponseDto> getRankTopTen();
}
