package com.ssafy.ssafit.global.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 사용자 인증 어노테이션
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})  // 메소드의 파라미터를 타겟으로 지정. 즉, 메소드의 파라미터(매개변수)를 대상으로 사용할 수 있다. 매개변수 앞에 붙이는 어노테이션이 된다.
@Retention(RetentionPolicy.RUNTIME)   // 어노테이션의 생명주기. 런타임동안 어노테이션이 살아있다.
public @interface LoginUser {
}

