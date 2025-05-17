package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;

public interface VideoService {

    VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto);
}

