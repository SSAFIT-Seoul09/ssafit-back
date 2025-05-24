package com.ssafy.ssafit.video.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private int views;

    public static Video from(Long userId, VideoRequestDto requestDto) {
        return Video.builder()
                .userId(userId)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .part(VideoPart.valueOf(requestDto.getPart()))
                .url(requestDto.getUrl())
                .views(0)
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

    public void update(VideoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.part = VideoPart.valueOf(requestDto.getPart());
        this.url = requestDto.getUrl();
        this.views = 0;
    }
}
