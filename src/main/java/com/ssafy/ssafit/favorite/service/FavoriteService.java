package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;

public interface FavoriteService {
    FavoriteResponseDto addFavorite(Long userId, Long videoId);
}
