package com.ssafy.ssafit.comment.service;

import com.ssafy.ssafit.comment.domain.model.Comment;
import com.ssafy.ssafit.comment.domain.repository.CommentDao;
import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j(topic = "CommentServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Transactional
    @Override
    public CommentResponseDto createComment(Long userId, Long reviewId, CommentRequestDto requestDto) {
        // 댓글 작성
        Comment comment = Comment.of(userId, reviewId, requestDto);

        // 디비 저장
        commentDao.insertComment(comment);

        // 조회
        Comment insertedComment = commentDao.findById(comment.getId());

        // 반환
        return CommentResponseDto.toDto(insertedComment);
    }

    @Override
    public List<CommentResponseDto> getComments(Long reviewId) {
        List<CommentResponseDto> list = commentDao.findByReviewId(reviewId);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("리뷰에 댓글이 없습니다.");
        }
        return list;
    }

    @Transactional
    @Override
    public CommentResponseDto update(Long userId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentDao.findById(commentId);

        // 댓글 존재 여부 확인
        if (comment == null) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        // 작성자와 수정하려는 사람이 같은지 확인.
        if(comment.getUserId() != userId) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.update(requestDto);

        int isUpdated = commentDao.updateComment(comment);
        if (isUpdated <= 0) {
            throw new IllegalArgumentException("댓글 수정에 문제가 생겼습니다.");
        }

        return CommentResponseDto.toDto(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentDao.findById(commentId);

        // 본인 댓글인지 확인
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        // 댓글 존재 유무 확인
        if(comment.getUserId() != userId) {
            throw new IllegalArgumentException("해당 댓글 수정 권한이 없습니다.");
        }

        // 댓글 삭제 성공 여부
        int isDeleted = commentDao.deleteById(commentId);
        if (isDeleted <= 0) {
            throw new IllegalArgumentException("댓글 삭제에 실패하였습니다.");
        }
    }
}
