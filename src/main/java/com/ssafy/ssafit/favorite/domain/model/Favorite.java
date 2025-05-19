package com.ssafy.ssafit.favorite.domain.model;

import com.ssafy.ssafit.favorite.dto.response.FavoriteResponseDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    private Long id;
    private Long userId;
    private Long videoId;

    public static Favorite of(Long userId, Long videoId) {
        return Favorite.builder()
                .userId(userId)
                .videoId(videoId)
                .build();
    }
}
