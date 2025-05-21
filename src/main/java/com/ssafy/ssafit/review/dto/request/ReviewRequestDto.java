package com.ssafy.ssafit.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private String title;
    private String content;
    private int rating;
}
