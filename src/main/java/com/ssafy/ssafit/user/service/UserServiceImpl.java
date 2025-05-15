package com.ssafy.ssafit.user.service;

import com.ssafy.ssafit.global.util.JwtUtil;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.dto.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.UserSignUpResponseDto;
import com.ssafy.ssafit.user.exception.EmailAlreadyExistException;
import com.ssafy.ssafit.user.exception.InvalidUserCredentialException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    @Override
    public UserSignUpResponseDto signup(UserSignUpRequestDto requestDto) {
        validateEmailDuplication(requestDto.getEmail());
        User user = User.from(requestDto);

        // 도메인 내부로 암호화 책임 위임
        user.encryptPassword(passwordEncoder);
        userDao.insertUser(user);

        User insertedUser = userDao.findUserByEmail(user.getEmail());

        return UserSignUpResponseDto.toDto(insertedUser);
    }

    // 회원가입
    @Override
    public UserSignInResponseDto login(UserSignInRequestDto requestDto, HttpServletResponse response) {
        User user = getUserByEmail(requestDto.getEmail());
        validatePassword(requestDto.getPassword(), user.getPassword());

        String token = jwtUtil.createToken(user.getId(), user.getRole());
        jwtUtil.addJwtToCookie(token, response);

        return UserSignInResponseDto.toDto(user, token);
    }

    private User getUserByEmail(String email) {
        return Optional.ofNullable(userDao.findUserByEmail(email))
                .orElseThrow(() -> InvalidUserCredentialException.ofEmail(email));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw InvalidUserCredentialException.ofPassword();
        }
    }

    private void validateEmailDuplication(String email) {
        if (userDao.existsByEmail(email)) {
            throw EmailAlreadyExistException.of(email);
        }
    }
}
