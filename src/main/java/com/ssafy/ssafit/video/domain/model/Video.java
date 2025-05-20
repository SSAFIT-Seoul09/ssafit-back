package com.ssafy.ssafit.video.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Video extends TimeStamped {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private VideoPart part;
    private String url;

    public static Video from(Long userId, VideoRequestDto requestDto) {
        return Video.builder()
                .userId(userId)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .part(VideoPart.valueOf(requestDto.getPart()))
                .url(requestDto.getUrl())
                .build();
    }

    public static Video from(VideoRequestDto requestDto) {
        return Video.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .part(VideoPart.valueOf(requestDto.getPart()))
                .url(requestDto.getUrl())
                .build();
    }
}
