package com.example.ch04.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ListOutputConverterService {

    private final RestClient restClient;

    public ListOutputConverterService(
            @Value("${spring.ai.openai.api-key}") String apiKey) {

        ReactorClientHttpRequestFactory requestFactory =
                new ReactorClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(120));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl("https://integrate.api.nvidia.com/v1")
                .defaultHeader(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .defaultHeader(
                        "Content-Type",
                        "application/json"
                )
                .build();
    }


    // NVIDIA API 공통 호출
    @SuppressWarnings("unchecked")
    private String callNvidia(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-ai/deepseek-v4-flash",

                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),

                "temperature", 1.0,
                "top_p", 0.95,
                "max_tokens", 1024,

                "chat_template_kwargs", Map.of(
                        "thinking", false
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("/chat/completions")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        log.info("NVIDIA API 전체 응답: {}", response);

        // choices 배열
        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        // choices[0]
        Map<String, Object> firstChoice =
                choices.get(0);

        // message
        Map<String, Object> message =
                (Map<String, Object>) firstChoice.get("message");

        // content
        String content =
                (String) message.get("content");

        log.info("NVIDIA Content: {}", content);

        return content;
    }


    // =====================================================
    // Low Level
    // =====================================================
    public List<String> convertLowLevel(String city) {

        ListOutputConverter converter =
                new ListOutputConverter();

        String prompt =
                city
                + "에서 유명한 호텔 목록 5개를 출력하세요. "
                + converter.getFormat();

        String answer =
                callNvidia(prompt);

        log.info(
                "Low Level 원본 응답: {}",
                answer
        );

        List<String> answerList =
                converter.convert(answer);

        log.info(
                "Low Level 변환 결과: {}",
                answerList
        );

        return answerList;
    }


    // =====================================================
    // High Level
    // =====================================================
    public List<String> convertHighLevel(String city) {

        ListOutputConverter converter =
                new ListOutputConverter();

        String prompt = """
                %s에서 유명한 호텔 목록 5개를 출력하세요.

                %s
                """.formatted(
                        city,
                        converter.getFormat()
                );

        String answer =
                callNvidia(prompt);

        log.info(
                "High Level 원본 응답: {}",
                answer
        );

        List<String> answerList =
                converter.convert(answer);

        log.info(
                "High Level 변환 결과: {}",
                answerList
        );

        return answerList;
    }
}