package com.ssafy.ssafit.usageTimeTracker.service;

import com.ssafy.ssafit.usageTimeTracker.domain.repository.UsageTimeTrackerDao;
import com.ssafy.ssafit.usageTimeTracker.dto.RankResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j(topic = "RankSystemServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankSystemServiceImpl implements RankSystemService {

    private final UsageTimeTrackerDao usageTimeTrackerDao;

    @Override
    public List<RankResponseDto> getRankTopTen() {
        List<RankResponseDto> list = usageTimeTrackerDao.getRankTopTen();
        return list;
    }
}
