package com.example.ch06.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;

@Service
public class ImageAnalysisService {

    private final ChatClient chatClient;

    public ImageAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<String> analysis(
            String question,
            String contentType,
            byte[] bytes) {

        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                    당신은 이미지 분석 전문가입니다.
                    사용자 질문에 맞게 이미지를 분석하고 한국어로 답변하세요.
                    """)
                .build();

        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(bytes)
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .build();

        return chatClient.prompt(prompt)
                .stream()
                .content()
                .onErrorResume(WebClientResponseException.class, error -> {
                    String responseBody = error.getResponseBodyAsString();

                    System.err.println(
                            "Ollama 오류 상태: " + error.getStatusCode()
                    );
                    System.err.println(
                            "Ollama 오류 본문: " + responseBody
                    );

                    return Flux.just(
                            "Ollama 요청 실패: " + responseBody
                    );
                });
    }
}