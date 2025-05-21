package com.ssafy.ssafit.review.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends TimeStamped {

    private Long id;
    private Long userId;
    private String content;
    private int rating;
}
