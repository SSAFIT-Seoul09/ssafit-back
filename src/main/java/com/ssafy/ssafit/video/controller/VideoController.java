package com.ssafy.ssafit.video.controller;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;
import com.ssafy.ssafit.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    // 영상 등록
    @PostMapping
    public ResponseEntity<ApiResponse<VideoResponseDto>> insertVideo(@LoginUser AuthenticatedUser authenticatedUser, @RequestBody VideoRequestDto requestDto) {
        Long userId = authenticatedUser.getUserId();
        log.info("사용자 ID: {}, 영상 등록 요청: {}", userId, requestDto.getTitle());

        VideoResponseDto responseDto = videoService.insertVideo(userId, requestDto);

        return ResponseEntity.ok(ApiResponse.success("영상 등록에 성공하였습니다.",responseDto));
    }


}
