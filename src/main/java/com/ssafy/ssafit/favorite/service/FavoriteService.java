package com.ssafy.ssafit.favorite.service;

import com.ssafy.ssafit.favorite.dto.UserFavoriteDto;
import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;

import java.util.List;

public interface FavoriteService {
    FavoriteResponseDto addFavorite(Long userId, Long videoId);

    List<UserFavoriteDto> getFavorites(Long userId);
}
