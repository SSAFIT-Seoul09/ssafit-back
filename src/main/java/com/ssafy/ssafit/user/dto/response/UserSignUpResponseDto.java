package com.ssafy.ssafit.user.dto.response;

import com.ssafy.ssafit.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserSignUpResponseDto {

    private Long userId;
    private String nickName;
    private String email;
    private int age;
    private LocalDateTime createdAt;

    public static UserSignUpResponseDto toDto(User user) {
        return UserSignUpResponseDto.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
