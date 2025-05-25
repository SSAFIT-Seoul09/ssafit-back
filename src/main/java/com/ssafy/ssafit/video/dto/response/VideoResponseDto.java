package com.ssafy.ssafit.video.dto.response;

import com.ssafy.ssafit.video.domain.model.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoResponseDto {

    private Long id;
    private String title;
    private String description;
    private String part;
    private String url;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static VideoResponseDto toDto(Video video) {
        return VideoResponseDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .part(video.getPart().getValue())
                .url(video.getUrl())
                .views(video.getViews())
                .createdAt(video.getCreatedAt())
                .modifiedAt(video.getModifiedAt())
                .build();
    }

}
