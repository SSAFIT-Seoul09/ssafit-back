package com.ssafy.ssafit.global.interceptor;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.UserContext;
import com.ssafy.ssafit.global.exception.ErrorCode;
import com.ssafy.ssafit.global.util.JwtUtil;
import com.ssafy.ssafit.global.util.exception.TokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 사용자가 요청을 넣을때, webconfig에서 열어놓지 않은 api로 요청을 넣을때 인터셉터가
 * 실행된다.
 */
@Slf4j(topic = "LoginInterceptor - JWT 토큰 로그인 검증")
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("요청 인터셉트: URI={}, Method={}", request.getRequestURI(), request.getMethod());

        // 1. 쿠키에서 토큰 까보기
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        if (tokenValue == null) {
            log.warn("JWT 토큰 없음 - 요청 거부: URI={}", request.getRequestURI());
            throw TokenException.of(ErrorCode.TOKEN_NOT_FOUND, "토큰이 존재하지 않습니다.");
        }

        // 2. Bearer 제거
        String token;
        token = jwtUtil.substringToken(tokenValue);

        // 3-1. 로그아웃 처리 된 토큰인지 확인
        if (jwtUtil.isTokenBlacklisted(token)) {
            log.warn("블랙리스트 처리된 JWT 토큰 - 접근 차단");
            throw TokenException.of(ErrorCode.BLACKLISTED_TOKEN, "로그아웃 된 토큰입니다.");
        }

        // 3-2. 토큰 유효성 검사
        jwtUtil.validateToken(token);

        // 4. 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(token);

        // 5. AuthenticatedUser 객체에 JWT토큰의 사용자 정보 저장
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(claims);

        // 6. ThreadLocal 저장
        UserContext.setUser(authenticatedUser);

        log.info("JWT 인증 성공: userId={}, role={}", authenticatedUser.getUserId(), authenticatedUser.getRole());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear(); // 요청 끝나면 반드시 클리어. securityContextHolder가 유저의 정보를 없애버리는 것과 같은 맥락
        log.debug("UserContext 클리어 완료 - 요청 종료: URI={}", request.getRequestURI());
    }
}
