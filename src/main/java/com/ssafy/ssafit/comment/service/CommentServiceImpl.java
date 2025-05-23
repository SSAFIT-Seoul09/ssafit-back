package com.ssafy.ssafit.comment.service;

import com.ssafy.ssafit.comment.domain.model.Comment;
import com.ssafy.ssafit.comment.domain.repository.CommentDao;
import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import com.ssafy.ssafit.comment.exception.*;
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
        log.info("댓글 작성 요청: userId={}, reviewId={}, requestDto={}", userId, reviewId, requestDto);

        // 댓글 작성
        Comment comment = Comment.of(userId, reviewId, requestDto);
        log.debug("댓글 객체 생성: {}", comment);

        // 디비 저장
        int isInserted = commentDao.insertComment(comment);
        if (isInserted <= 0) {
            log.info("댓글 등록 실패 : userId : {}, reviewId: {}, {}", userId, reviewId, requestDto);
            throw CommentInsertException.of(userId);
        }
        log.info("댓글 DB 저장 완료: commentId={}", comment.getId());

        // 조회
        Comment insertedComment = commentDao.findById(comment.getId());
        if(insertedComment != null) {
            log.error("댓글 삽입 실패: commentId={}", comment.getId());
            throw CommentNotFoundException.of();
        }

        log.info("댓글 작성 성공: commentId={}", insertedComment.getId());
        // 반환
        return CommentResponseDto.toDto(insertedComment);
    }

    @Override
    public List<CommentResponseDto> getComments(Long reviewId) {
        log.info("리뷰에 대한 댓글 조회 요청: reviewId={}", reviewId);
        List<CommentResponseDto> list = commentDao.findByReviewId(reviewId);
        if (list.isEmpty()) {
            log.error("댓글 없음: reviewId={}", reviewId);
            throw CommentNotFoundException.of();
        }
        log.info("댓글 조회 성공: reviewId={}, 댓글 수={}", reviewId, list.size());
        return list;
    }

    @Transactional
    @Override
    public CommentResponseDto update(Long userId, Long commentId, CommentRequestDto requestDto) {
        log.info("댓글 수정 요청: userId={}, commentId={}, requestDto={}", userId, commentId, requestDto);


        // 댓글 존재 여부 확인
        Comment comment = getComment(commentId);

        // 작성자와 수정하려는 사람이 같은지 확인.
        if(comment.getUserId() != userId) {
            log.error("수정 권한 없음: userId={}는 commentId={}의 수정 권한이 없습니다.", userId, commentId);
            throw CommentAccessForbiddenException.of(commentId);
        }

        comment.update(requestDto);
        log.debug("댓글 수정 내용: {}", comment);

        int isUpdated = commentDao.updateComment(comment);
        if (isUpdated <= 0) {
            log.error("댓글 수정 실패: commentId={}", commentId);
            throw CommentUpdateException.of(commentId);
        }

        log.info("댓글 수정 성공: commentId={}", commentId);
        return CommentResponseDto.toDto(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        log.info("댓글 삭제 요청: userId={}, commentId={}", userId, commentId);


        // 댓글 존재 유무 확인
        Comment comment = getComment(commentId);

        // 본인 댓글인지 확인
        if(comment.getUserId() != userId) {
            log.error("삭제 권한 없음: userId={}는 commentId={}의 삭제 권한이 없습니다.", userId, commentId);
            throw CommentAccessForbiddenException.of(commentId);
        }

        // 댓글 삭제 성공 여부
        int isDeleted = commentDao.deleteById(commentId);
        if (isDeleted <= 0) {
            log.error("댓글 삭제 실패: commentId={}", commentId);
            throw CommentDeleteException.of(commentId);
        }
        log.info("댓글 삭제 성공: commentId={}", commentId);
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentDao.findById(commentId);
        if (comment == null) {
            log.error("댓글 없음: commentId={}", commentId);
            throw CommentNotFoundException.of();
        }
        return comment;
    }
}
