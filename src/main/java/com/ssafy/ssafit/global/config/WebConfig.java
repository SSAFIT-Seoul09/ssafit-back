package com.ssafy.ssafit.global.config;

import com.ssafy.ssafit.global.auth.LoginUserArgumentResolver;
import com.ssafy.ssafit.global.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    // Bcrypt암호화 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 로그인 검증 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/users/signin", "/api/users/signup", "/css/**", "/js/**", "/images/**") // 현재 로그인, 회원가입 요청을 제외하면 모든 요청에 jwt토큰 검증을 진행함.
                //.excludePathPatterns("/api/videos/search", "/api/videos/*")  // 비디오 검색
                .excludePathPatterns("/api/videos/search", "/api/videos/{id:[\\d]+}")  // 비디오 검색
                .excludePathPatterns("/api/reviews/*");
    }

    // @LoginUser어노테이션 리졸버 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://localhost:5173",
                        "http://localhost:5173/"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키 인증(JWT 등) 필요시 true
    }

}
