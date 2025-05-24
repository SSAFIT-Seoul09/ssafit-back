package com.ssafy.ssafit.comment.service;

import com.ssafy.ssafit.comment.domain.model.Comment;
import com.ssafy.ssafit.comment.domain.repository.CommentDao;
import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import com.ssafy.ssafit.comment.exception.*;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "CommentServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final UserDao userDao;

    @Transactional
    @Override
    public CommentResponseDto createComment(Long userId, Long reviewId, CommentRequestDto requestDto) {
        log.info("댓글 작성 요청: userId={}, reviewId={}, requestDto={}", userId, reviewId, requestDto);

        isUserValid(userId);

        Comment comment = Comment.of(userId, reviewId, requestDto);

        // 디비 저장
        int isInserted = commentDao.insertComment(comment);
        if (isInserted <= 0) {
            log.info("댓글 등록 실패 : userId : {}, reviewId: {}, {}", userId, reviewId, requestDto);
            throw CommentInsertException.of(userId);
        }
        log.info("댓글 DB 저장 완료: commentId={}", comment.getId());

        CommentResponseDto responseDto = commentDao.getCommentResponseDtoByCommentId(comment.getId());
        if (responseDto == null) {
            log.error("댓글 삽입 실패: commentId={}", responseDto.getId());
            throw CommentNotFoundException.of();
        }

        log.info("댓글 작성 성공: commentId={}", responseDto.getId());
        return responseDto;
    }

    @Override
    public List<CommentResponseDto> getComments(Long reviewId) {
        log.info("리뷰에 대한 댓글 조회 요청: reviewId={}", reviewId);
        List<CommentResponseDto> list = commentDao.getCommentResponseDtoByReviewId(reviewId);

        log.info("댓글 조회 성공: reviewId={}, 댓글 수={}", reviewId, list.size());
        return list;
    }

    @Transactional
    @Override
    public CommentResponseDto update(Long userId, Long commentId, CommentRequestDto requestDto) {
        log.info("댓글 수정 요청: userId={}, commentId={}, requestDto={}", userId, commentId, requestDto);

        isUserValid(userId);

        Comment comment = commentDao.findById(commentId);

        isWrittenByUserId(userId, commentId, comment);

        comment.update(requestDto);
        log.debug("댓글 수정 내용: {}", comment);

        int isUpdated = commentDao.updateComment(comment);
        if (isUpdated <= 0) {
            log.error("댓글 수정 실패: commentId={}", commentId);
            throw CommentUpdateException.of(commentId);
        }

        CommentResponseDto responseDto = commentDao.getCommentResponseDtoByCommentId(commentId);

        log.info("댓글 수정 성공: commentId={}", responseDto.getId());
        return responseDto;
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        log.info("댓글 삭제 요청: userId={}, commentId={}", userId, commentId);

        isUserValid(userId);

        Comment comment = commentDao.findById(commentId);

        isWrittenByUserId(userId, commentId, comment);

        // 댓글 삭제 성공 여부
        int isDeleted = commentDao.deleteById(commentId);
        if (isDeleted <= 0) {
            log.error("댓글 삭제 실패: commentId={}", commentId);
            throw CommentDeleteException.of(commentId);
        }
        log.info("댓글 삭제 성공: commentId={}", commentId);
    }


    // 존재하는 회원의 요청인지 확인
    private boolean isUserValid(Long userId) {
        User user = Optional.ofNullable(userDao.findUserById(userId))
                .orElseThrow(() -> {
                    log.error("해당 userId : {}는 존재하지 않는 회원입니다.", userId);
                    throw UserNotFoundException.ofUserId(userId);
                });
        return user != null;
    }

    // 요청자가 작성한 글인지 확인
    private static void isWrittenByUserId(Long userId, Long commentId, Comment comment) {
        if(!Objects.equals(comment.getUserId(), userId)) {
            log.error("수정 권한 없음: userId={}는 commentId={}의 수정 권한이 없습니다.", userId, commentId);
            throw CommentAccessUnauthorizedException.of(commentId);
        }
    }
}
