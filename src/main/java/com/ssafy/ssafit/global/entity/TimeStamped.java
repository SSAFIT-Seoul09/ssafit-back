package com.ssafy.ssafit.global.entity;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class TimeStamped {

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
