package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.global.exception.ErrorCode;
import com.ssafy.ssafit.video.domain.model.Video;
import com.ssafy.ssafit.video.domain.model.VideoPart;
import com.ssafy.ssafit.video.domain.repository.VideoDao;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;
import com.ssafy.ssafit.video.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j(topic = "videoServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService {

    private final VideoDao videoDao;

    // 영상 등록
    @Transactional
    @Override
    public VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto) {
        log.info("영상 등록 시작: userId={}, videoRequestDto={}", userId, requestDto);

        Video video = Video.from(userId, requestDto);
        videoDao.insertVideo(video);

        Video insertedVideo = videoDao.findVideoById(video.getId());
        log.info("영상 등록 완료: videoId={}", video.getId());

        return VideoResponseDto.toDto(insertedVideo);
    }

    // 영상 전체 조회 + 조건 검색 통합
    @Override
    public List<VideoResponseDto> searchVideos(String title, List<String> parts, Integer views) {
        String order = null;
        if (views != null) {
            order = views == 0 ? "ASC" : "DESC";
        }

        log.info("검색 시작 - 제목: {}, 파트: {}, 조회수: {}, 정렬: {}", title, parts, views, order);
        List<Video> videos = videoDao.searchVideos(title, parts, order);

        log.info("검색된 영상 목록 개수: {}", videos.size());

        return videos.stream()
                .map(video -> VideoResponseDto.toDto(video))
                .toList();
    }


    // 영상 상세 조회
    @Override
    public VideoResponseDto getVideoById(Long videoId) {
        log.info("영상 상세 조회 시작: videoId={}", videoId);
        Video video = getVideo(videoId);

        log.info("영상 상세 조회 완료: {}", video);
        return VideoResponseDto.toDto(video);
    }

    // 영상 수정
    @Transactional
    @Override
    public VideoResponseDto updateVideo(Long userId, Long videoId, VideoRequestDto requestDto) {
        log.info("영상 수정 시작: videoId={}, userId={}, requestDto={}", videoId, userId, requestDto);

        Video video = getVideo(videoId);

        // 영상 정보 수정
        video.setUserId(userId);
        video.setTitle(requestDto.getTitle());
        video.setDescription(requestDto.getDescription());
        video.setPart(VideoPart.from(requestDto.getPart()));
        video.setUrl(requestDto.getUrl());

        videoDao.updateVideo(video);

        log.info("영상 수정 완료: videoId={}", videoId);

        Video updatedVideo = videoDao.findVideoById(videoId);
        log.info("수정된 영상 정보: {}", updatedVideo);

        return VideoResponseDto.toDto(updatedVideo);
    }

    // 영상 삭제
    @Transactional
    @Override
    public void deleteVideo(Long userId, Long videoId) {
        log.info("영상 삭제 시작: videoId={}, userId={}", videoId, userId);

        Video video = getVideo(videoId);

        videoDao.deleteVideo(video.getId());
        log.info("영상 삭제 완료: videoId={}", videoId);
    }

    private Video getVideo(Long videoId) {
        Video video = Optional.ofNullable(videoDao.findVideoById(videoId))
                .orElseThrow(() -> {
                    log.error("영상 조회 실패: videoId={} (영상 없음)", videoId);
                    throw VideoNotFoundException.of(ErrorCode.VIDEO_NOT_FOUND);
                });
        return video;
    }
}
