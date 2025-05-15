package com.ssafy.ssafit.global.interceptor;


import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.UserContext;
import com.ssafy.ssafit.global.util.JwtUtil;
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
@Slf4j(topic = "JWT 토큰 로그인 검증")
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    /**
     * 로그인 여부를 확인하고, 로그인되지 않았다면 false, or else trues
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 쿠키에서 토큰 까보기
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        if (tokenValue == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 2. Bearer 제거
        String token;
        token = jwtUtil.substringToken(tokenValue);

        // 3. 토큰 유효성 검사
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 4. 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(token);

        // 5. AuthenticatedUser 객체에 JWT토큰의 사용자 정보 저장
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(claims);

        // 6. ThreadLocal에 저장
        UserContext.setUser(authenticatedUser);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear(); // 요청 끝나면 반드시 클리어. securityContextHolder가 유저의 정보를 없애버리는 것과 같은 맥락
    }
}
