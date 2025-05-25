package com.ssafy.ssafit.api;

import com.ssafy.ssafit.api.dto.KeywordCategoryResult;
import com.ssafy.ssafit.api.dto.VideoRecommendationResult;
import com.ssafy.ssafit.api.dto.YoutubeMeta;
import com.ssafy.ssafit.api.dto.YoutubeVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "ChatGPTController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VideoRecommnedController {

    @Value("${youtube-key}") String youtubeApiKey;
    @Value("${chatGPT-key}") String openaiApiKey;
    private final ChatGPTService chatGPTService;
    private final YoutubeService youtubeService;

    // gpt 요청 테스트
//    @GetMapping("/search")
//    public String recommendVideoGPT(@RequestParam("body") String query) throws Exception {
//        log.info("GPT 요청 시작 : 요청 들어온 query : {}", query);
//        return chatGPTService.getYoutubeSummary(query);
//    }


    // 1. 유튜브 api로 유저의 영상 메타데이터를 받아옴.
    // 2. 해당 메타데이터를 GPT API로 전송. gpt에게서 키워드 추천을 받음.
    // 3. 유튜브 API로 gpt 추천 키워드로 재검색. 해당 링크를 프론트에게 전달
    // 4. 프론트에서 해당 영상을 공개
    @GetMapping("/youtube/recommend")
    public VideoRecommendationResult recommend(@RequestParam("videoUrl") String videoUrl) throws Exception {
        log.info("유튜브 영상 추천 요청 시작 - recommend video url {}", videoUrl);

        // 1. videoId 추출
        String videoId = youtubeService.extractVideoId(videoUrl);
        if (videoId == null) throw new IllegalArgumentException("유효하지 않은 유튜브 URL");

        // 2. 메타데이터 수집
        YoutubeMeta meta = youtubeService.fetchYoutubeMeta(videoId, youtubeApiKey);

        // 3. GPT로 카테고리 + 관련 키워드 추천
        KeywordCategoryResult result = chatGPTService.getCategoryAndKeywords(meta, openaiApiKey);
        String category = result.category();
        List<String> keywords = result.keywords();

        // 4. 키워드별로 유튜브 재검색 + 5분 이상만 필터
        List<YoutubeVideo> all = new ArrayList<>();
        for (String keyword : keywords) {
            all.addAll(youtubeService.searchYoutubeByKeyword(keyword, youtubeApiKey));
        }

        return new VideoRecommendationResult(category, all);
    }
}
