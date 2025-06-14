package com.ssafy.ssafit.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafit.api.dto.YoutubeMeta;
import com.ssafy.ssafit.api.dto.YoutubeVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j(topic = "YoutubeService")
@Service
public class YoutubeService {

    // 예: https://www.youtube.com/watch?v=dQw4w9WgXcQ or https://youtu.be/dQw4w9WgXcQ -> "dQw4w9WgXcQ"만 추출(v= 는 일반 주소, be=는 짧은 주소)
    public String extractVideoId(String url) {
        String pattern = "(?:v=|be/)([a-zA-Z0-9_-]{11})";  // 뒤에 11자리 추출 정규식 표현
        Matcher matcher = Pattern.compile(pattern).matcher(url);
        return matcher.find() ? matcher.group(1) : null;  // 11자리를 찾았으면 group(1) or null
    }

    public YoutubeMeta fetchYoutubeMeta(String videoId, String apiKey) {
//        String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&key=" + apiKey;
//        WebClient webClient = WebClient.create();
//        String response = webClient.get()
//                .uri(url)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();

        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com")
                .build();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("items");
            if (items.isArray() && !items.isEmpty()) {
                JsonNode snippet = items.get(0).path("snippet");
                return new YoutubeMeta(
                        snippet.path("title").asText(),
                        snippet.path("description").asText(),
                        snippet.path("channelTitle").asText()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<YoutubeVideo> searchYoutubeByKeyword(String keyword, String apiKey) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com")
                .build();

        // 1차: 검색으로 videoId 리스트 얻기
        String searchResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", keyword)
                        .queryParam("maxResults", "1") //
                        .queryParam("type", "video")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        List<String> videoIds = new ArrayList<>();
        List<YoutubeVideo> result = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(searchResponse);
            JsonNode items = root.path("items");
            for (JsonNode item : items) {
                videoIds.add(item.path("id").path("videoId").asText());
            }
        } catch (Exception e) {
            log.info("searchYoutubeByKeyword error : 1차 videoId 리스트 얻기에서 실패");
            e.printStackTrace();
        }

        if (videoIds.isEmpty()) return result;

        // 2차: videoId로 /videos API에서 duration(길이) 정보 받아오기
        String ids = String.join(",", videoIds);
        String videosResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/videos")
                        .queryParam("part", "snippet,contentDetails")
                        .queryParam("id", ids)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(videosResponse);
            JsonNode items = root.path("items");
            for (JsonNode item : items) {
                String durationStr = item.path("contentDetails").path("duration").asText(); // ISO 8601
                int seconds = parseYoutubeDuration(durationStr);
                if (seconds >= 300) { // 5분 이상만. 쇼츠 불러오는거 막기 + 5분 이하의 운동은 운동답지 않을 가능성이 높기 때문
                    String videoId = item.path("id").asText();
                    JsonNode snippet = item.path("snippet");
                    result.add(new YoutubeVideo(
                            snippet.path("title").asText(),
                            "https://www.youtube.com/watch?v=" + videoId,
                            snippet.path("thumbnails").path("default").path("url").asText(),
                            seconds
                    ));
                }
            }
        } catch (Exception e) {
            log.info("searchYoutubeByKeyword error : 2차 videoId로 영상 길이 받아오기 실패");
            e.printStackTrace();
        }

        return result;
    }

    // ISO 8601 duration (PT5M17S 같은 형식) → 초로 변환
    private int parseYoutubeDuration(String duration) {
        Pattern pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?");
        Matcher matcher = pattern.matcher(duration);
        int hours = 0, minutes = 0, seconds = 0;
        if (matcher.matches()) {
            if (matcher.group(1) != null) hours = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) minutes = Integer.parseInt(matcher.group(2));
            if (matcher.group(3) != null) seconds = Integer.parseInt(matcher.group(3));
        }
        return hours * 3600 + minutes * 60 + seconds;
    }
}
