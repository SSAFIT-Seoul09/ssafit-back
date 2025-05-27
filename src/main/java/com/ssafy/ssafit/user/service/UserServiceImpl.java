package com.ssafy.ssafit.user.service;

import com.ssafy.ssafit.global.util.JwtUtil;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.dto.request.UpdateUserDetailRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignInRequestDto;
import com.ssafy.ssafit.user.dto.request.UserSignUpRequestDto;
import com.ssafy.ssafit.user.dto.response.UserDetailResponseDTO;
import com.ssafy.ssafit.user.dto.response.UserPostCntResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignInResponseDto;
import com.ssafy.ssafit.user.dto.response.UserSignUpResponseDto;
import com.ssafy.ssafit.user.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j(topic = "UserServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Override
    @Transactional
    public UserSignUpResponseDto signup(UserSignUpRequestDto requestDto) {
        log.info("회원가입 요청: email={}, nickname={}", requestDto.getEmail(), requestDto.getNickname());
        // 이메일 중복 검사
        validateEmailDuplication(requestDto.getEmail());

        User user = User.from(requestDto);
        log.debug("User 객체 생성 완료: {}", user);

        // 비밀번호 암호화
        user.encryptPassword(passwordEncoder);
        log.debug("비밀번호 암호화 완료");

        // 회원 저장
        int inserted = userDao.insertUser(user);
        if (inserted > 0) {
            log.info("회원가입 성공: userId={}, email={}", user.getId(), user.getEmail());

            User savedUser = userDao.findUserById(user.getId());
            return UserSignUpResponseDto.toDto(savedUser);
        } else {
            log.error("회원가입 실패: email={}", requestDto.getEmail());
            throw SignUpFailureException.of("회원가입에 실패하였습니다.");
        }
    }

    // 로그인
    @Override
    public UserSignInResponseDto login(UserSignInRequestDto requestDto, HttpServletResponse response) {
        log.info("로그인 요청: email={}", requestDto.getEmail());
        User user = getUserByEmail(requestDto.getEmail());

        validatePassword(requestDto.getPassword(), user.getPassword());

        String token = jwtUtil.createToken(user.getId(), user.getNickname(), user.getRole());
        jwtUtil.addJwtToCookie(token, response);

        log.info("로그인 성공: userId={}, email={}", user.getId(), user.getEmail());
        return UserSignInResponseDto.toDto(user, token);
    }

    // 로그아웃
    @Override
    public void logout(HttpServletRequest request) {
        log.info("로그아웃 요청");

        // 1. 쿠키에서 토큰 꺼내기
        String token = jwtUtil.getTokenFromRequest(request);
        if (token == null) {
            log.warn("로그아웃 실패 - 토큰 없음");
            throw UserNotFoundException.of("토큰이 없습니다");
        }

        // 2. 블랙리스트에 추가
        jwtUtil.addBlacklistToken(token);
        log.info("로그아웃 성공 - 토큰 블랙리스트에 추가됨");
    }

    // 회원정보 조회
    @Override
    public UserDetailResponseDTO getUserInfo(Long userId) {
        log.info("회원 정보 조회 요청: userId={}", userId);
        User user = userDao.findUserById(userId);
        if(user == null) {
            log.warn("회원 정보 조회 실패 - 존재하지 않음: userId={}", userId);
            throw UserNotFoundException.ofUserId(userId);
        }

        log.debug("회원 정보 조회 성공: userId={}", userId);
        return UserDetailResponseDTO.toDto(user);
    }

    // 회원정보 수정
    @Override
    @Transactional
    public UserDetailResponseDTO updateUser(Long userId, UpdateUserDetailRequestDto requestDto) {
        log.info("회원 정보 수정 요청: userId={}", userId);

        // 등록된 회원인지 확인
        User checkUser = userDao.findUserById(userId);
        isRegisteredUser(userId, checkUser);

        User user = User.from(userId, requestDto);
        if (user.getPassword() != null) {
            user.encryptPassword(passwordEncoder);
            log.debug("비밀번호 암호화 완료: userId={}", userId);
        }

        // 회원 업데이트
        if (userDao.updateUser(user) > 0) {
            log.info("회원 정보 수정 성공: userId={}", userId);
            return UserDetailResponseDTO.toDto(user);
        } else {
            log.error("회원 정보 수정 실패(DB update 실패): userId={}", userId);
            throw UserUpdateFailureException.of(userId);
        }
    }

    // 회원탈퇴
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("회원 탈퇴 요청: userId={}", userId);

        if (userDao.deleteUser(userId) < 0) {
            log.warn("회원 탈퇴 실패 - 존재하지 않음: userId={}", userId);
            throw UserDeleteFailureException.of(userId);
        }

        log.info("회원 탈퇴 성공: userId={}", userId);
    }

    // 게시글 개수 조회
    @Override
    public UserPostCntResponseDto getUserPostCnt(Long userId) {
        User user = userDao.findUserById(userId);

        // 회원 검증
        isRegisteredUser(userId, user);

        // 게시글 개수 조회
        UserPostCntResponseDto responseDto = userDao.getUserPostCnt(userId);
        responseDto.setUserId(userId);

        return responseDto;
    }

    private User getUserByEmail(String email) {
        log.debug("이메일로 회원 조회: email={}", email);

        return Optional.ofNullable(userDao.findUserByEmail(email))
                .orElseThrow(() -> {
                    log.warn("회원 조회 실패 - 존재하지 않는 이메일: {}", email);
                    return InvalidUserCredentialException.ofEmail(email);
                });
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            log.warn("비밀번호 불일치");
            throw InvalidUserCredentialException.ofPassword();
        }

        log.debug("비밀번호 일치 확인 완료");
    }

    private void validateEmailDuplication(String email) {
        if (userDao.existsByEmail(email)) {
            log.warn("이메일 중복 회원가입 시도: email={}", email);
            throw EmailAlreadyExistException.of(email);
        }
        log.debug("이메일 중복 없음 확인 완료: email={}", email);
    }

    private static void isRegisteredUser(Long userId, User checkUser) {
        if (checkUser == null) {
            log.warn("회원 정보 수정 실패 - 존재하지 않음: userId={}", userId);
            throw UserNotFoundException.ofUserId(userId);
        }
    }
}
