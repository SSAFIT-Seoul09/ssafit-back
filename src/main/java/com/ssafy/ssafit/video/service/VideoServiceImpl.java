package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.video.domain.model.Video;
import com.ssafy.ssafit.video.domain.model.VideoPart;
import com.ssafy.ssafit.video.domain.repository.VideoDao;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoDao videoDao;

    // 영상 등록
    @Override
    public VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto) {
        Video video = Video.from(userId, requestDto);
        videoDao.insertVideo(video);

        Video insertedVideo = videoDao.findVideoById(video.getId());
        return VideoResponseDto.toDto(insertedVideo);
    }

    // 영상 전체 조회 + 조건 검색 통합
    @Override
    public List<VideoResponseDto> searchVideos(String title, List<String> parts, Integer views) {
        String order = null;
        if (views != null) {
            order = views == 0 ? "ASC" : "DESC";
        }

        log.info("검색 방식: {}", order);
        List<Video> videos = videoDao.searchVideos(title, parts, order);

        log.info("영상 제목 : {}, 파트 : {}, 조회수 : {}", title, parts, order);

        return videos.stream()
                .map(video -> VideoResponseDto.toDto(video))
                .toList();
    }


    // 영상 상세 조회
    @Override
    public VideoResponseDto getVideoById(Long videoId) {
        Video video = Optional.ofNullable(videoDao.findVideoById(videoId))
                .orElseThrow(() -> new IllegalArgumentException("추후 재정의"));
        return VideoResponseDto.toDto(video);
    }

    // 영상 수정
    @Override
    public VideoResponseDto updateVideo(Long userId, Long videoId, VideoRequestDto requestDto) {
        Video video = Optional.ofNullable(videoDao.findVideoById(videoId))
                .orElseThrow(() -> new IllegalArgumentException("추후 재정의"));

        // 영상 정보 수정
        video.setUserId(userId);
        video.setTitle(requestDto.getTitle());
        video.setDescription(requestDto.getDescription());
        video.setPart(VideoPart.from(requestDto.getPart()));
        video.setUrl(requestDto.getUrl());

        videoDao.updateVideo(video);
        Video updatedVideo = videoDao.findVideoById(videoId);
        return VideoResponseDto.toDto(updatedVideo);
    }

    // 영상 삭제
    @Override
    public void deleteVideo(Long userId, Long videoId) {
        Video video = Optional.ofNullable(videoDao.findVideoById(videoId))
                .orElseThrow(() -> new IllegalArgumentException("추후 재정의"));
        videoDao.deleteVideo(video.getId());
    }
}
