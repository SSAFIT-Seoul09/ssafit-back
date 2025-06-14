package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import com.ssafy.ssafit.favorite.dto.response.UserFavoriteResponseDto;

import java.util.List;

public interface FavoriteService {
    FavoriteResponseDto addFavorite(Long userId, Long videoId);

    List<UserFavoriteResponseDto> getFavorites(Long userId);
}
