package com.ssafy.ssafit.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafit.api.dto.KeywordCategoryResult;
import com.ssafy.ssafit.api.dto.YoutubeMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "ChatGPTService")
@Service
public class ChatGPTService {

    public KeywordCategoryResult getCategoryAndKeywords(YoutubeMeta meta, String openaiApiKey) throws Exception {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String prompt = """
    다음은 유튜브 운동 영상의 정보입니다.

    제목: %s
    설명: %s

    위 영상을 보고,
    1. 영상에 가장 적합한 우리 서비스의 운동부위 카테고리(ARM, LEG, BACK, CHEST, FULL_BODY) 하나만 골라서 반환해줘.
    2. 비슷한 운동 유튜브 영상을 검색할 때 사용할 키워드를 3개만 한 줄에 하나씩 추천해줘.

    반드시 아래 형식으로만 답변해줘(설명, 불필요한 말, 번호 등 추가하지 마).
    카테고리:[카테고리]
    키워드:
    [키워드1]
    [키워드2]
    [키워드3]
    """.formatted(meta.title(), meta.description());

        // 요청 바디 생성
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = new HashMap<>();
//        body.put("model", "gpt-3.5-turbo");
        body.put("model", "gpt-4o");
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "운동 영상 추천 카테고리 및 키워드 생성기"));
        messages.add(Map.of("role", "user", "content", prompt));
        body.put("messages", messages);

        String requestBody = mapper.writeValueAsString(body);

        String response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root = mapper.readTree(response);
        String gptReply = root.path("choices").get(0).path("message").path("content").asText();

        String[] lines = gptReply.split("\\R");
        String category = null;
        List<String> keywords = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("카테고리:")) {
                category = line.substring("카테고리:".length()).trim();
            } else if (!line.startsWith("키워드:") && !line.trim().isEmpty()) {
                keywords.add(line.trim());
            }
        }
        return new KeywordCategoryResult(category, keywords);
    }
}

