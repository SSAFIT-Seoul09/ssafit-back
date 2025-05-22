package com.ssafy.ssafit.comment.controller;

import com.ssafy.ssafit.comment.dto.request.CommentRequestDto;
import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import com.ssafy.ssafit.comment.service.CommentService;
import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "CommentController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{reviewId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@LoginUser AuthenticatedUser authenticatedUser,
                                                                         @PathVariable(name = "reviewId") Long reviewId,
                                                                         @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.createComment(authenticatedUser.getUserId(), reviewId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 작성이 완료 되었습니다.", responseDto));
    }

    // 댓글 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(@PathVariable(name = "reviewId") Long reviewId) {
        List<CommentResponseDto> list = commentService.getComments(reviewId);
        return ResponseEntity.ok(ApiResponse.success("댓글을 전부 조회하였습니다.", list));
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(@LoginUser AuthenticatedUser authenticatedUser,
                                                                         @PathVariable(name = "commentId") Long commentId,
                                                                         @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.update(authenticatedUser.getUserId(), commentId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 수정에 성공하였습니다.", responseDto));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@LoginUser AuthenticatedUser authenticatedUser,
                                                           @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(authenticatedUser.getUserId(), commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글 삭제에 성공하였습니다"));
    }
}
