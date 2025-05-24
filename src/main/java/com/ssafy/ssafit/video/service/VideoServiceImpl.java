package com.ssafy.ssafit.video.service;

import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.exception.UserNotFoundException;
import com.ssafy.ssafit.video.domain.model.Video;
import com.ssafy.ssafit.video.domain.repository.VideoDao;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import com.ssafy.ssafit.video.dto.VideoResponseDto;
import com.ssafy.ssafit.video.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "videoServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService {

    private final VideoDao videoDao;
    private final UserDao userDao;

    // 영상 등록
    @Transactional
    @Override
    public VideoResponseDto insertVideo(Long userId, VideoRequestDto requestDto) {
        log.info("영상 등록 시작: userId={}, videoRequestDto={}", userId, requestDto);

        // 해당 회원의 존재 여부
        isUserValid(userId);

        Video video = Video.from(userId, requestDto);
        int inserted = videoDao.insertVideo(video);
        if (inserted <= 0) {
            log.error("영상 등록 실패: userId={}, videoRequestDto={}", userId, requestDto);
            throw VideoInsertException.of("영상 등록에 실패하였습니다.");
        }

        Video insertedVideo = videoDao.findVideoById(video.getId());
        log.info("영상 등록 완료: videoId={}", video.getId());

        return VideoResponseDto.toDto(insertedVideo);
    }

    // 영상 전체 조회 + 조건 검색 통합
    @Override
    public List<VideoResponseDto> searchVideos(String title, List<String> parts, Integer views) {
        String order = null;
        if (views != null) {
            order = views == 0 ? null : "DESC";
        }

        log.info("검색 시작 - 제목: {}, 파트: {}, 조회수: {}, 정렬여부(null or DESC): {}", title, parts, views, order);
        List<Video> videos = videoDao.searchVideos(title, parts, order);

        log.info("검색된 영상 목록 개수: {}", videos.size());

        return videos.stream()
                .map(video -> VideoResponseDto.toDto(video))
                .toList();
    }


    // 영상 상세 조회
    @Transactional
    @Override
    public VideoResponseDto getVideoById(Long videoId) {
        log.info("영상 상세 조회 시작: videoId={}", videoId);
        videoDao.increaseViewCnt(videoId);

        Video video = getVideo(videoId);

        log.info("영상 상세 조회 완료: {}", video);
        return VideoResponseDto.toDto(video);
    }

    // 영상 수정
    @Transactional
    @Override
    public VideoResponseDto updateVideo(Long userId, Long videoId, VideoRequestDto requestDto) {
        log.info("영상 수정 시작: videoId={}, userId={}, requestDto={}", videoId, userId, requestDto);

        isUserValid(userId);

        Video video = getVideo(videoId);

        isWrittenByUserId(userId, videoId, video);

        // 영상 정보 수정
        video.update(requestDto);

        int isUpdated = videoDao.updateVideo(video);
        if (isUpdated <= 0) {
            log.error("영상 등록 실패: videoId={}, videoRequestDto={}", videoId, requestDto);
            throw VideoUpdatedException.of(videoId);
        }

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

        isUserValid(userId);

        Video video = getVideo(videoId);

        isWrittenByUserId(userId, videoId, video);

        int isDeleted = videoDao.deleteVideo(video.getId());
        if (isDeleted <= 0) {
            log.info("영상 삭제 실패: videoId={}", videoId);
            throw VideoDeleteException.of(videoId);
        }
        log.info("영상 삭제 완료: videoId={}", videoId);
    }

    private Video getVideo(Long videoId) {
        Video video = Optional.ofNullable(videoDao.findVideoById(videoId))
                .orElseThrow(() -> {
                    log.error("영상 조회 실패: videoId={} (영상 없음)", videoId);
                    throw VideoNotFoundException.of(videoId);
                });
        return video;
    }

    // 존재하는 회원의 요청인지 확인
    private boolean isUserValid(Long userId) {
        User user = Optional.ofNullable(userDao.findUserById(userId))
                .orElseThrow(() -> {
                    log.error("해당 userId : {}는 존재하지 않는 회원입니다.", userId);
                    throw UserNotFoundException.ofUserId(userId);
                });
        return user != null;
    }

    // 요청자가 작성한 글인지 확인
    private void isWrittenByUserId(Long userId, Long videoId, Video video) {
        if (!Objects.equals(video.getUserId(), userId)) {
            log.info("본인 글이 아닙니다. 작성자Id : {} 수정요청Id : {}", videoId, userId);
            throw VideoAccessUnauthorizedException.of(video.getUserId(), userId);
        }
    }
}
