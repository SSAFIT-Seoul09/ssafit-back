package com.ssafy.ssafit.comment.service;

import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(Long userId, Long reviewId, CommentRequestDto requestDto);

    List<CommentResponseDto> getComments(Long reviewId);

    CommentResponseDto update(Long userId, Long commentId, CommentRequestDto requestDto);

    void deleteComment(Long userId, Long commentId);

    List<CommentResponseDto> getMyCommentList(Long userId);
}
