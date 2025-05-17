package com.ssafy.ssafit.video.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoListResponseDto {

    private List<VideoResponseDto> videoList;

}
