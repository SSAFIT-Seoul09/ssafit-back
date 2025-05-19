package com.ssafy.ssafit.video.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRequestDto {

    private String title;
    private String description;
    private String part;
    private String url;
}

