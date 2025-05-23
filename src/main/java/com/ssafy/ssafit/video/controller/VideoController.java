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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        log.info("사용자 ID : {}, 영상 등록 요청 : {}", userId, requestDto.getTitle());

        VideoResponseDto responseDto = videoService.insertVideo(userId, requestDto);

        return ResponseEntity.ok(ApiResponse.success("영상 등록에 성공하였습니다.", responseDto));
    }

    // 영상 전체 조회 + 조건 검색 통합
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<VideoResponseDto>>> searchVideos(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "parts", required = false) List<String> parts,
            @RequestParam(name = "views", required = false) Integer views) {

        log.info("영상 제목 : {}, 파트 : {}, 조회수 : {}",
                title, parts, views);

        List<VideoResponseDto> responseList = videoService.searchVideos(title, parts, views);
        return ResponseEntity.ok(ApiResponse.success("영상 조회에 성공하였습니다.", responseList));
    }

    // 영상 상세 조회
    @GetMapping("/{videoId}")
    public ResponseEntity<ApiResponse<VideoResponseDto>> getVideoById(@PathVariable(name = "videoId") Long videoId) {
        VideoResponseDto responseDto = videoService.getVideoById(videoId);
        return ResponseEntity.ok(ApiResponse.success("영상 상세 조회에 성공하였습니다.", responseDto));
    }

    // 영상 수정
    @PutMapping("/{videoId}")
    public ResponseEntity<ApiResponse<VideoResponseDto>> updateVideo(
            @LoginUser AuthenticatedUser user,
            @PathVariable(name = "videoId") Long videoId,
            @RequestBody VideoRequestDto requestDto) {
        VideoResponseDto responseDto = videoService.updateVideo(user.getUserId(), videoId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("영상 정보 수정에 성공하였습니다.", responseDto));
    }

    // 영상 삭제
    @DeleteMapping("/{videoId}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(
            @LoginUser AuthenticatedUser authenticatedUser,
            @PathVariable(name = "videoId") Long videoId) {
        Long userId = authenticatedUser.getUserId();
        log.info("사용자 ID: {}, 영상 Id: {}", userId, videoId);

        videoService.deleteVideo(userId, videoId);
        return ResponseEntity.ok(ApiResponse.success("영상 삭제에 성공하였습니다.", null));
    }

}
