package com.ssafy.ssafit.global.auth;

import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @LoginUSer 어노테이션을 사용하면 현재 로그인한 사용자의 정보를 자동으로 주입해주는 역할을 해주는 클래스.
 *
 * 요청으로 쿠키에 JWT토큰을 받고,
 * JWT토큰에서 사용자의 정보를 추출해서 AuthenticatedUser객체에 저장을 한다.
 * AuthenticatedUser객체에 저장된 값을 UserContext라는 ThreadHold에 저장해둔다.
 * @LoginUser어노테이션을 쓰면 해당 값을 불러와 자동으로 주입받을 수 있다.
 */
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    // 해당 파라미터를 이 리졸버가 처리할 것인지 판단을 한다. LoginUser어노테이션인지 확인하는 메소드.
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) &&
                parameter.getParameterType().equals(AuthenticatedUser.class);
    }

    // 파라미터에 실제로 주입할 객체를 리턴해준다.
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        // 현재 저장된 유저의 정보를 가져온다.
        AuthenticatedUser authenticatedUser = UserContext.getUser();
        if (authenticatedUser == null)
            throw new RuntimeException("인증된 사용자가 없습니다.");
        return authenticatedUser;
    }
}

