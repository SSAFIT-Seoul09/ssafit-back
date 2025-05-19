package com.ssafy.ssafit.user.dto.response;

import com.ssafy.ssafit.user.domain.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDetailResponseDTO {

    private String email;
    private String nickname;
    private int age;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserDetailResponseDTO toDto(User user) {
        return UserDetailResponseDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .role(user.getRole().getValue())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }

}
