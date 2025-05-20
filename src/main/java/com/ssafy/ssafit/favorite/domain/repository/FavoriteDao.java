package com.ssafy.ssafit.favorite.domain.repository;

import com.ssafy.ssafit.favorite.domain.model.Favorite;
import com.ssafy.ssafit.favorite.dto.UserFavoriteDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteDao {

    // 찜 여부 확인
    boolean isFavorite(Favorite favorite);

    // 찜 추가
    void insertFavorite(Favorite favorite);

    // 찜 제거
    void removeFavorite(Favorite favorite);

    List<UserFavoriteDto> getAllById(@Param("userId") Long userId);
}
