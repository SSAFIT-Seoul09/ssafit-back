package com.ssafy.ssafit.global.auth;

import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.model.UserRole;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * JWT에서 사용자의 정보 userId, 권한(role)을 저장할 객체
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthenticatedUser {

    private Long userId;
    private UserRole role;

    public static AuthenticatedUser of(Claims claims) {
        return AuthenticatedUser.builder()
                .userId(Long.parseLong(claims.getSubject()))
                .role(UserRole.valueOf((String) claims.get("auth")))
                .build();
    }

    public static AuthenticatedUser of(User user) {
        return AuthenticatedUser.builder()
                .userId(user.getId())
                .role(user.getRole())
                .build();
    }

    public static AuthenticatedUser of(Long userId, UserRole role) {
        return AuthenticatedUser.builder()
                .userId(userId)
                .role(role)
                .build();
    }
}