package com.ssafy.ssafit.global.interceptor;


import com.ssafy.ssafit.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 사용자가 요청을 넣을때, webconfig에서 열어놓지 않은 api로 요청을 넣을때 인터셉터가
 * 실행된다.
 */
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

        // 5. 사용자 정보 request에 저장 (필요시 다른 곳에서 꺼내 쓸 수 있도록)
        request.setAttribute("userNiceName", claims.getSubject());  // 사용자의 닉네임 넣어둠
        request.setAttribute("userRole", claims.get(JwtUtil.AUTHORIZATION_KEY));  // 사용자의 권한 넣어둠.

        return true;
    }
}
