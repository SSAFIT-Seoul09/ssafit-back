package com.ssafy.ssafit.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserDetailRequestDto {

    private String email;
    private String password;
    private String nickname;
    private int age;
    private String role;
}
