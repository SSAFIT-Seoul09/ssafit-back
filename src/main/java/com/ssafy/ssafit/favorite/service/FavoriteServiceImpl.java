package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.domain.model.Favorite;
import com.ssafy.ssafit.favorite.domain.repository.FavoriteDao;
import com.ssafy.ssafit.favorite.dto.UserFavoriteDto;
import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import com.ssafy.ssafit.favorite.exception.FavoriteException;
import com.ssafy.ssafit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j(topic = "FavoriteServiceImpl")
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteDao favoriteDao;

    @Override
    public FavoriteResponseDto addFavorite(Long userId, Long videoId) {
        log.info("사용자 ID: {}과 영상 ID: {}에 대한 찜 작업이 시작되었습니다.", userId, videoId);

        Favorite favorite = Favorite.of(userId, videoId);

        // 이미 찜을 한 항목인지 확인.
        boolean isFavorite = favoriteDao.isFavorite(favorite);
        log.info("사용자 ID: {}꽈 영상 ID: {}의 찜 여부: {}", userId, videoId, isFavorite);

        // 찜을 했으면, 제거
        if (isFavorite) {
            favoriteDao.removeFavorite(favorite);
            log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜이 취소되었습니다.", userId, videoId);
        } else {
            favoriteDao.insertFavorite(favorite);
            log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜이 등록되었습니다.", userId, videoId);
        }

        log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜 작업이 완료되었습니다.", userId, videoId);
        return FavoriteResponseDto.of(favorite);
    }

    @Override
    public List<UserFavoriteDto> getFavorites(Long userId) {
        log.info("사용자 ID: {}에 대한 찜 목록 조회 요청", userId);

        List<UserFavoriteDto> favorites = favoriteDao.getAllById(userId);
        if (favorites.isEmpty()) {
            log.error("사용자 ID: {}에 대한 찜 목록이 비어있음", userId);
            throw FavoriteException.of(ErrorCode.FAVORITE_NOT_FOUND); // 찜 목록이 비어있을 경우
        }

        log.info("사용자 ID: {}에 대한 찜 목록 조회 성공, 개수: {}", userId, favorites.size());
        return favorites;
    }
}
