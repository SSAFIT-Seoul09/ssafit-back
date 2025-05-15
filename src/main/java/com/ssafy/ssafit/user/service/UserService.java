package com.ssafy.ssafit.user.service;

import com.ssafy.ssafit.user.dto.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.UserSignUpResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserSignUpResponseDto signup(UserSignUpRequestDto requestDto);

    UserSignInResponseDto login(UserSignInRequestDto requestDto, HttpServletResponse res);
}
