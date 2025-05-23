package com.ssafy.ssafit.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserPostCntResponseDto {

    @Setter
    private Long userId;
    private int videoCount;
    private int reviewCount;
    private int commentCount;
    private int favoriteCount;
}
