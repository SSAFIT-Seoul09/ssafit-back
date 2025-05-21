package com.ssafy.ssafit.comment.domain.model;

import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.global.entity.TimeStamped;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends TimeStamped {

    private Long id;
    private Long userId;
    private Long reviewId;
    private String content;

    public static Comment of(Long userId, Long reviewId, CommentRequestDto requestDto) {
        return Comment.builder()
                .userId(userId)
                .reviewId(reviewId)
                .content(requestDto.getContent())
                .build();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
