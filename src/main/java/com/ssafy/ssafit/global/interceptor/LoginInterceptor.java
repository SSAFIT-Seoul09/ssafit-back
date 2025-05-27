package com.ssafy.ssafit.global.interceptor;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.UserContext;
import com.ssafy.ssafit.global.exception.ErrorCode;
import com.ssafy.ssafit.global.util.JwtUtil;
import com.ssafy.ssafit.global.util.exception.TokenException;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Slf4j(topic = "LoginInterceptor - JWT 토큰 로그인 검증")
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDao userDao;
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 0. Options 처리
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.debug("OPTIONS 요청 - 인증 생략: URI={}", uri);
            return true;
        }

        // 0-1.각 도메인별로 경로 예외처리
        // 로그인 회원가입
        if (isPublicForUser(request)) {
            log.info("로그인 및 회원가입 인증 제외 : URI={}, Method={}", uri, method);
            return true;
        }
        // 비디오 도메인 경로 제외
        if (isPublicGetForVideo(request)) {
            log.info("Video GET 요청 인증 제외 : URI={}, Method={}", uri, method);
            return true;
        }
        // 리뷰 도메인 경로 제외
        if (isPublicGetForReview(request)) {
            log.info("Review GET 요청 인증 제외 : URI={}, Method={}", uri, method);
            return true;
        }
        // 댓글
        if(isPublicGetForComment(request)) {
            log.info("Comment GET 요청 인증 제외 : URI={}, Method={}", uri, method);
            return true;
        }

        log.info("요청 인터셉트: URI={}, Method={}", uri, method);
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

        // 5. 사용자 유효성 검사
        log.info("사용자 유효성 검사");
        Long userId = Long.parseLong(claims.getSubject());
        User user = Optional.ofNullable(userDao.findUserById(userId))
                .orElseThrow(() -> {
                    log.error("해당 userId : {}는 존재하지 않는 회원입니다.", userId);
                    return UserNotFoundException.ofUserId(userId);
                });

        // 6. AuthenticatedUser 객체에 JWT토큰의 사용자 정보 저장
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(user);

        // 7. ThreadLocal 저장
        UserContext.setUser(authenticatedUser);

        log.info("JWT 인증 성공: userId={}, role={}", authenticatedUser.getUserId(), authenticatedUser.getRole());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear(); // 요청 끝나면 반드시 클리어. securityContextHolder 유저의 정보를 없애버리는 것과 같은 맥락
        log.debug("UserContext 클리어 완료 - 요청 종료: URI={}", request.getRequestURI());
    }

    private boolean isPublicForUser(HttpServletRequest req) {
        String uri = req.getRequestURI();

        return antPathMatcher.match(uri, "/api/users/signin")
                || antPathMatcher.match(uri, "/api/users/signup");
    }

    private boolean isPublicGetForReview(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        return "GET".equalsIgnoreCase(method) && (
                antPathMatcher.match("/api/reviews", uri)
                        || antPathMatcher.match("/api/reviews/*", uri)
        );
    }

    private boolean isPublicGetForVideo(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        return "GET".equalsIgnoreCase(method) && (
                antPathMatcher.match("/api/videos/*", uri)
                        || antPathMatcher.match("/api/videos/search", uri)
        );
    }

    private boolean isPublicGetForComment(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        return "GET".equalsIgnoreCase(method) &&
                antPathMatcher.match("/api/comments/*", uri);
    }

}
