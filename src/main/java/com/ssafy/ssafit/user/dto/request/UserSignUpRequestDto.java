package com.ssafy.ssafit.user.dto.request;

import lombok.Getter;

@Getter
public class UserSignUpRequestDto {

    private String nickname;
    private int age;
    private String email;
    private String password;
}
