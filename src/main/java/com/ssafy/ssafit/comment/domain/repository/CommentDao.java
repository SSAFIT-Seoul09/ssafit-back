package com.ssafy.ssafit.comment.domain.repository;

import com.ssafy.ssafit.comment.domain.model.Comment;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDao {

    int insertComment(Comment comment);

    Comment findById(Long id);

    List<CommentResponseDto> findByReviewId(@Param("reviewId") Long reviewId);

    int updateComment(Comment comment);

    int deleteById(Long commentId);
}
