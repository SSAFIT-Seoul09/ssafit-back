package com.ssafy.ssafit.user.dto;

import com.ssafy.ssafit.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSignInResponseDto {
    private Long userId;
    private String token;

    public static UserSignInResponseDto toDto(User user, String token) {
        return UserSignInResponseDto.builder()
                .userId(user.getId())
                .token(token)
                .build();
    }
}
