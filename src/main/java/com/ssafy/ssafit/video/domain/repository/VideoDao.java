package com.ssafy.ssafit.video.domain.repository;

import com.ssafy.ssafit.video.domain.model.Video;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoDao {

    // 영상 등록
    int insertVideo(Video video);

    // ID로 영상 조회
    Video findVideoById(@Param("id")Long id);

    // 조건 검색 및 전체 조회
    List<Video> searchVideos(@Param("title") String title, @Param("parts")List<String> parts, @Param("order")String order);

    // 영상 수정
    int updateVideo(Video video);

    // 영상 삭제
    int deleteVideo(@Param("id")Long id);

    // 조회수 증가
    void increaseViewCnt(@Param("videoId") Long videoId);
}
