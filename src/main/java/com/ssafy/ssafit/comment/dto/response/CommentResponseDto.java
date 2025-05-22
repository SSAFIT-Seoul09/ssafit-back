package com.ssafy.ssafit.comment.dto.response;

import com.ssafy.ssafit.comment.domain.model.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private Long commentId;
    private Long userId;
    private Long reviewId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentResponseDto toDto(Comment insertedComment) {
        return CommentResponseDto.builder()
                .commentId(insertedComment.getId())
                .userId(insertedComment.getUserId())
                .reviewId(insertedComment.getReviewId())
                .content(insertedComment.getContent())
                .createdAt(insertedComment.getCreatedAt())
                .modifiedAt(insertedComment.getModifiedAt())
                .build();
    }
}
