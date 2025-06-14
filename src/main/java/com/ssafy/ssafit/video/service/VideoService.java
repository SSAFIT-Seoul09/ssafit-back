package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.video.dto.request.VideoRequestDto;
import com.ssafy.ssafit.video.dto.response.VideoResponseDto;

import java.util.List;

public interface VideoService {

    // 영상 등록
    VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto);

    // 영상 전체 조회 및 조건 검색 통합
    List<VideoResponseDto> searchVideos(String title, List<String> parts, Integer views);


    // 영상 상세 조회 (ID 기준)
    VideoResponseDto getVideoById(Long videoId);

    // 영상 수정
    VideoResponseDto updateVideo(Long userId, Long videoId, VideoRequestDto requestDto);

    // 영상 삭제
    void deleteVideo(Long userId, Long videoId);

    // 회원이 등록한 영상 목록 조회
    List<VideoResponseDto> getMyVideoList(Long userId);
}
