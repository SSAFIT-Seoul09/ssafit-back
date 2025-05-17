package com.ssafy.ssafit.video.domain.repository;

import com.ssafy.ssafit.video.domain.model.Video;
import java.util.List;

public interface VideoDao {

    // 영상 등록
    void insertVideo(Video video);

    // ID로 영상 조회
    Video findById(Long id);

    // 전체 영상 목록 조회
    List<Video> findAll();

    // 제목으로 영상 조회
    Video findByTitle(String title);

    // 영상 정보 수정
    void updateVideo(Video video);

    // 영상 삭제
    void deleteVideo(Long id);
}
