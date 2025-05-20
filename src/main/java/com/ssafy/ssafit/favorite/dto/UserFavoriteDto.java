package com.ssafy.ssafit.favorite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFavoriteDto {

    private Long id;
    private Long videoId;
    private String title;
    private String description;
    private String part;
    private String url;
}
