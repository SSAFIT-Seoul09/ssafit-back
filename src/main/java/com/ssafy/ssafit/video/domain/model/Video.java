package com.ssafy.ssafit.video.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import com.ssafy.ssafit.video.dto.VideoRequestDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video extends TimeStamped {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private VideoPart part;
    private String url;

    /**
     * [등록 시 사용]
     * VideoRequestDto와 userId를 기반으로 Video 객체 생성
     * 주로 영상 등록(insert) 시 사용되며, userId는 별도로 전달 받음
     *
     * @param userId 로그인한 사용자 ID
     * @param requestDto 클라이언트로부터 전달받은 영상 요청 데이터
     * @return 도메인 객체 Video
     */
    public static Video from(Long userId, VideoRequestDto requestDto) {
        return Video.builder()
                .userId(userId)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .part(VideoPart.valueOf(requestDto.getPart()))
                .url(requestDto.getUrl())
                .build();
    }


    /**
     * [수정 시 사용]
     * userId가 DTO에 포함된 경우에 사용하는 팩토리 메서드
     * 주로 영상 수정(update) 시 사용되며, id와 userId가 DTO에 있다고 가정
     *
     * @param requestDto 클라이언트로부터 전달받은 영상 요청 데이터
     * @return 도메인 객체 Video
     */
    public static Video from(VideoRequestDto requestDto) {
        return Video.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .part(VideoPart.valueOf(requestDto.getPart()))
                .url(requestDto.getUrl())
                .build();
    }

}
