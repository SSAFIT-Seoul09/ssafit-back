package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.domain.model.Favorite;
import com.ssafy.ssafit.favorite.domain.repository.FavoriteDao;
import com.ssafy.ssafit.favorite.dto.UserFavoriteDto;
import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
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
        log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜 작업이 시작되었습니다.", userId, videoId);

        Favorite favorite = Favorite.of(userId, videoId);

        // 이미 찜을 한 항목인지 확인.
        boolean isFavorite = favoriteDao.isFavorite(favorite);
        log.info("사용자 ID: {}와 영상 ID: {}의 찜 여부: {}", userId, videoId, isFavorite);

        // 찜을 했으면, 제거
        if (isFavorite) {
            favoriteDao.removeFavorite(favorite);
            log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜이 취소되었습니다.", userId, videoId);
        } else {
            favoriteDao.insertFavorite(favorite);
            log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜이 추가되었습니다.", userId, videoId);
        }

        log.info("사용자 ID: {}와 영상 ID: {}에 대한 찜 작업이 완료되었습니다.", userId, videoId);
        return FavoriteResponseDto.of(favorite);
    }

    @Override
    public List<UserFavoriteDto> getFavorites(Long userId) {
        return favoriteDao.getAllById(userId);
    }
}
