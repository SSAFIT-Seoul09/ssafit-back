package com.ssafy.ssafit.user.controller;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.user.dto.request.UpdateUserDetailRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.response.UserDetailResponseDTO;
import com.ssafy.ssafit.user.dto.response.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignUpResponseDto;
import com.ssafy.ssafit.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j(topic = "UserController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserSignUpResponseDto>> signup(@RequestBody UserSignUpRequestDto requestDto) {
        log.info("회원가입 요청: {}", requestDto.getEmail());
        UserSignUpResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입에 성공하였습니다.", responseDto));
    }
    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<UserSignInResponseDto>> login(@RequestBody UserSignInRequestDto requestDto, HttpServletResponse res, HttpServletRequest req) {
        log.info("로그인 요청: {}", requestDto.getEmail());
        UserSignInResponseDto responseDto = userService.login(requestDto, res);
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    log.debug("JWT 쿠키 발견: {}", cookie.getName());
                    log.info("쿠키 헤더 토큰 확인해보기 : {}", URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));
                }
            }
        }
        log.info("응답객체 토큰: {}", responseDto.getToken());
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", responseDto));
    }

    // 로그아웃
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        log.info("로그아웃 컨트롤러 요청");
        userService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("로그아웃에 성공하였습니다"));
    }

    // 회원 정보 조회
    @GetMapping
    public ResponseEntity<ApiResponse<UserDetailResponseDTO>> getUserInfo(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원정보 조회: {}", authenticatedUser.getUserId());
        UserDetailResponseDTO responseDto = userService.getUserInfo(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원 정보 조회에 성공하였습니다.", responseDto));
    }

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<ApiResponse<UserDetailResponseDTO>> updateUser(@LoginUser AuthenticatedUser authenticatedUser,
                                                                         @RequestBody UpdateUserDetailRequestDto requestDto) {
        log.info("회원정보 수정: {}", authenticatedUser.getUserId());
        UserDetailResponseDTO responseDTO = userService.updateUser(authenticatedUser.getUserId(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정에 성공하였습니다.", responseDTO));
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@LoginUser AuthenticatedUser authenticatedUser) {
        log.info("회원탈퇴 : {}", authenticatedUser.getUserId());
        userService.deleteUser(authenticatedUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴에 성공하였습니다."));
    }
}
