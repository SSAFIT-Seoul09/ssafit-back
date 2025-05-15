package com.ssafy.ssafit.user.domain.model;


import com.ssafy.ssafit.global.entity.TimeStamped;
import com.ssafy.ssafit.user.dto.UserSignUpRequestDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends TimeStamped {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int age;
    private UserRole role;

    public static User from(UserSignUpRequestDto requestDto) {
        return User.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .nickname(requestDto.getNickname())
                .age(requestDto.getAge())
                .role(UserRole.USER)
                .build();
    }

    // 비밀번호 암호화
    public void encryptPassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }
}

