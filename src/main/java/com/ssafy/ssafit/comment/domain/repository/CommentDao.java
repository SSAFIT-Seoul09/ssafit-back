package com.ssafy.ssafit.comment.domain.repository;

import com.ssafy.ssafit.comment.domain.model.Comment;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDao {

    int insertComment(Comment comment);

    CommentResponseDto getCommentResponseDtoByCommentId(@Param("commentId") Long commentId);

    List<CommentResponseDto> getCommentResponseDtoByReviewId(@Param("reviewId") Long reviewId);

    Comment findById(Long id);

    int updateComment(Comment comment);

    int deleteById(@Param("commentId") Long commentId);

    List<CommentResponseDto> getCommentResponseDtoByUserId(@Param("userId") Long userId);
}
