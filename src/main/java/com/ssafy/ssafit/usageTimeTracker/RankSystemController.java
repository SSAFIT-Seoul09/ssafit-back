package com.ssafy.ssafit.usageTimeTracker;

import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.usageTimeTracker.dto.RankResponseDto;
import com.ssafy.ssafit.usageTimeTracker.service.RankSystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j(topic = "RankSystemController")
@RestController
@RequiredArgsConstructor
public class RankSystemController {

    private final RankSystemService rankSystemService;

    @GetMapping("/api/rank")
    public ResponseEntity<ApiResponse<List<RankResponseDto>>> getRankTopTen() {
        List<RankResponseDto> list = rankSystemService.getRankTopTen();
        return ResponseEntity.ok(ApiResponse.success("이벤트를 위한 TOP 10 사용자 조회에 성공하였습니다.", list));
    }
}
