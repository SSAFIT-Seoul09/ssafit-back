package com.ssafy.ssafit.favorite.controller;

import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import com.ssafy.ssafit.favorite.dto.response.UserFavoriteResponseDto;
import com.ssafy.ssafit.favorite.service.FavoriteService;
import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "FavoriteController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 찜 등록, 제거
    @PostMapping("/{videoId}")
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(@LoginUser AuthenticatedUser authenticatedUser,
                                                                        @PathVariable Long videoId) {
        log.info("회원ID={} addFavorite videoId={}", authenticatedUser.getUserId(), videoId);
        FavoriteResponseDto responseDto = favoriteService.addFavorite(authenticatedUser.getUserId(), videoId);
        return ResponseEntity.ok(ApiResponse.success("찜 작업 완료되었습니다.", responseDto));
    }

    // 찜 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserFavoriteResponseDto>>> getFavorites(@LoginUser AuthenticatedUser authenticatedUser) {
        List<UserFavoriteResponseDto> list = favoriteService.getFavorites(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("사용자 찜 목록 조회에 성공하였습니다.", list));
    }
}
