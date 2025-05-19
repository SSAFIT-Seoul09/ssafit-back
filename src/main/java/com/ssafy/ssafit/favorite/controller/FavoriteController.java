package com.ssafy.ssafit.favorite.controller;

import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import com.ssafy.ssafit.favorite.service.FavoriteService;
import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "FavoriteController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 찜 등록
    @PostMapping("/{videoId}/favorites")
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> addFavorite(@LoginUser AuthenticatedUser authenticatedUser,
                                                                        @PathVariable Long videoId) {
        log.info("회원ID={} addFavorite videoId={}", authenticatedUser.getUserId(), videoId);
        FavoriteResponseDto responseDto = favoriteService.addFavorite(authenticatedUser.getUserId(), videoId);
        return ResponseEntity.ok(ApiResponse.success("찜 등록이 완료되었습니다.", responseDto));
    }

    // 찜 제거


    // 찜 조회
}
