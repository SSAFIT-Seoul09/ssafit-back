package com.ssafy.ssafit.user.controller;

import com.ssafy.ssafit.comment.dto.response.CommentResponseDto;
import com.ssafy.ssafit.comment.service.CommentService;
import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import com.ssafy.ssafit.review.service.ReviewService;
import com.ssafy.ssafit.user.dto.request.UpdateUserDetailRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.response.UserDetailResponseDTO;
import com.ssafy.ssafit.user.dto.response.UserPostCntResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignUpResponseDto;
import com.ssafy.ssafit.user.service.UserService;
import com.ssafy.ssafit.video.dto.response.VideoResponseDto;
import com.ssafy.ssafit.video.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "UserController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final VideoService videoService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserSignUpResponseDto>> signup(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        log.info("회원가입 요청: {}", requestDto.getEmail());
        UserSignUpResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입에 성공하였습니다.", responseDto));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<UserSignInResponseDto>> login(@RequestBody UserSignInRequestDto requestDto, HttpServletResponse res) {
        log.info("로그인 요청: {}", requestDto.getEmail());
        UserSignInResponseDto responseDto = userService.login(requestDto, res);
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", responseDto));
    }

    // 로그아웃
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        log.info("로그아웃 요청");
        userService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("로그아웃에 성공하였습니다"));
    }

    // 회원 정보 조회
    @GetMapping
    public ResponseEntity<ApiResponse<UserDetailResponseDTO>> getUserInfo(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원정보 조회 - 회원ID: {}", authenticatedUser.getUserId());
        UserDetailResponseDTO responseDto = userService.getUserInfo(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원 정보 조회에 성공하였습니다.", responseDto));
    }

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<ApiResponse<UserDetailResponseDTO>> updateUser(@LoginUser AuthenticatedUser authenticatedUser,
                                                                         @RequestBody UpdateUserDetailRequestDto requestDto) {
        log.info("회원정보 수정 - 회원ID: {}", authenticatedUser.getUserId());
        UserDetailResponseDTO responseDTO = userService.updateUser(authenticatedUser.getUserId(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정에 성공하였습니다.", responseDTO));
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원탈퇴 - 회원ID: {}", authenticatedUser.getUserId());
        userService.deleteUser(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴에 성공하였습니다."));
    }

    // 작성 글 개수 조회
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserPostCntResponseDto>> getMyInfo(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원 게시물 개수 조회 - 회원ID: {}", authenticatedUser.getUserId());
        UserPostCntResponseDto responseDto = userService.getUserPostCnt(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("게시글 개수 조회 성공", responseDto));
    }

    // 작성한 비디오 전체 조회
    @GetMapping("/info/videos")
    public ResponseEntity<ApiResponse<List<VideoResponseDto>>> getMyVideos(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원 등록 영상 조회 - 회원ID: {}", authenticatedUser.getUserId());
        List<VideoResponseDto> list = videoService.getMyVideoList(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원이 등록한 영상 조회 성공", list));
    }

    // 작성한 리뷰 전체 조회
    @GetMapping("/info/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getMyReviews(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원 등록 리뷰 조회 - 회원ID: {}", authenticatedUser.getUserId());
        List<ReviewResponseDto> list = reviewService.getMyReviewList(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원이 등록한 영상 조회 성공", list));
    }

    // 작성한 댓글 전체 조회
    @GetMapping("/info/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getMyComments(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원 등록 댓글 조회 - 회원ID: {}", authenticatedUser.getUserId());
        List<CommentResponseDto> list = commentService.getMyCommentList(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원이 등록한 댓글 조회 성공", list));
    }
}
