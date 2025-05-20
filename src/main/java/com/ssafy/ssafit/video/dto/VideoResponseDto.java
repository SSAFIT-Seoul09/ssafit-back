package com.ssafy.ssafit.video.dto;

import com.ssafy.ssafit.video.domain.model.Video;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoResponseDto {

    private Long videoId;
    private String title;
    private String description;
    private String part;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static VideoResponseDto toDto(Video video) {
        return VideoResponseDto.builder()
                .videoId(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .part(video.getPart().getValue())
                .url(video.getUrl())
                .createdAt(video.getCreatedAt())
                .modifiedAt(video.getModifiedAt())
                .build();
    }

}
