package com.ssafy.ssafit.favorite.dto.response;

import com.ssafy.ssafit.favorite.domain.model.Favorite;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteResponseDto {
    private Long favoriteId;
    private Long videoId;

    public static FavoriteResponseDto of(Favorite favorite) {
        return FavoriteResponseDto.builder()
                .favoriteId(favorite.getId())
                .videoId(favorite.getVideoId())
                .build();
    }
}