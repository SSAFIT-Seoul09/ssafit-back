package com.ssafy.ssafit.user.service;

import com.ssafy.ssafit.user.dto.request.UpdateUserDetailRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.response.UserDetailResponseDTO;
import com.ssafy.ssafit.user.dto.response.UserPostCntResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignUpResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    UserSignUpResponseDto signup(UserSignUpRequestDto requestDto);

    UserSignInResponseDto login(UserSignInRequestDto requestDto, HttpServletResponse res);

    UserDetailResponseDTO getUserInfo(Long userId);

    UserDetailResponseDTO updateUser(Long userId, UpdateUserDetailRequestDto requestDto);

    void deleteUser(Long userId);

    void logout(HttpServletRequest request);

    UserPostCntResponseDto getUserPostCnt(Long userId);
}
