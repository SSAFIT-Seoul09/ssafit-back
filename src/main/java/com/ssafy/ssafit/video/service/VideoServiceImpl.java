package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.video.domain.model.Video;
import com.ssafy.ssafit.video.domain.repository.VideoDao;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{

    private final VideoDao videoDao;

    // 영상 등록
    @Override
    public VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto) {
        Video video = Video.from(userId, requestDto);  // 팩토리 메서드로 도메인 객체 생성
        videoDao.insertVideo(video);

        Video insertedVideo = videoDao.findById(video.getId()); // 저장된 영상 다시 조회
        return VideoResponseDto.toDto(insertedVideo);
    }


}
