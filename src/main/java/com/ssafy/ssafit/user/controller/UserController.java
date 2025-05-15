package com.ssafy.ssafit.user.controller;

import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.user.dto.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.UserSignUpResponseDto;
import com.ssafy.ssafit.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserSignUpResponseDto>> signup(@RequestBody UserSignUpRequestDto requestDto) {
        log.info("회원가입 요청: {}", requestDto.getEmail());
        UserSignUpResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입에 성공하였습니다.", responseDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<UserSignInResponseDto>> login(@RequestBody UserSignInRequestDto requestDto, HttpServletResponse res) {
        log.info("로그인 요청: {}", requestDto.getEmail());
        UserSignInResponseDto responseDto = userService.login(requestDto, res);
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", responseDto));

    }
}
