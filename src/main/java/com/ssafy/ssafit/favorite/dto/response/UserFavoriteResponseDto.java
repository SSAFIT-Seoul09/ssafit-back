package com.ssafy.ssafit.favorite.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFavoriteResponseDto {

    private Long favoriteId;
    private Long videoId;
    private String title;
    private String description;
    private String part;
    private String url;
    private int views;
}
