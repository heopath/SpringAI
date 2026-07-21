package com.example.ch04.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class NvidiaApiService {

    private final RestClient restClient;

    public NvidiaApiService(
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

    @SuppressWarnings("unchecked")
    public String call(String prompt) {

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

        Map<String, Object> response =
                restClient.post()
                        .uri("/chat/completions")
                        .body(requestBody)
                        .retrieve()
                        .body(Map.class);

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        Map<String, Object> message =
                (Map<String, Object>) choices
                        .get(0)
                        .get("message");

        String content =
                (String) message.get("content");

        log.info("NVIDIA Content: {}", content);

        return content;
    }


    // System Message를 사용할 때
    @SuppressWarnings("unchecked")
    public String callWithSystemMessage(
            String systemMessage,
            String userMessage) {

        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-ai/deepseek-v4-flash",

                "messages", List.of(

                        Map.of(
                                "role", "system",
                                "content", systemMessage
                        ),

                        Map.of(
                                "role", "user",
                                "content", userMessage
                        )
                ),

                "temperature", 1.0,
                "top_p", 0.95,
                "max_tokens", 1024,

                "chat_template_kwargs", Map.of(
                        "thinking", false
                )
        );

        Map<String, Object> response =
                restClient.post()
                        .uri("/chat/completions")
                        .body(requestBody)
                        .retrieve()
                        .body(Map.class);

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        Map<String, Object> message =
                (Map<String, Object>) choices
                        .get(0)
                        .get("message");

        return (String) message.get("content");
    }
}