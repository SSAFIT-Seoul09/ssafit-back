package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.domain.model.Favorite;
import com.ssafy.ssafit.favorite.domain.repository.FavoriteDao;
import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "FavoriteServiceImpl")
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteDao favoriteDao;

    @Override
    public FavoriteResponseDto addFavorite(Long userId, Long videoId) {
        Favorite favorite = Favorite.of(userId, videoId);
        favoriteDao.insertFavorite(favorite);
        return FavoriteResponseDto.of(favorite);
    }
}
