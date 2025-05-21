package com.ssafy.ssafit.video.domain.repository;

import com.ssafy.ssafit.video.domain.model.Video;

import java.util.List;

public interface VideoDao {

    // 영상 등록
    void insertVideo(Video video);

    // ID로 영상 조회
    Video findVideoById(Long id);

    // 조건 검색 및 전체 조회
    List<Video> searchVideos(String title, List<String> parts, String order);

    // 영상 수정
    void updateVideo(Video video);

    // 영상 삭제
    void deleteVideo(Long id);

}
