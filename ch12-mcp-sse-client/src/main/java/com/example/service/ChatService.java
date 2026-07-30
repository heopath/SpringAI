package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            ToolCallbackProvider toolCallbackProvider) {

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        사용자의 요청에 필요한 경우 반드시 MCP 도구를 사용하세요.
                        도구 실행 결과를 받으면 빈 응답을 반환하지 말고,
                        결과를 한국어 문장으로 정리하여 최종 답변하세요.
                        """)
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    public String chat(String question) {

        log.info("사용자 질문: {}", question);

        ChatResponse response = chatClient.prompt()
                .user(question)
                .call()
                .chatResponse();

        if (response == null || response.getResult() == null) {
            log.error("ChatResponse 또는 Result가 null입니다.");
            return "AI 응답을 받지 못했습니다.";
        }

        String answer = response.getResult()
                .getOutput()
                .getText();

        log.info("전체 응답: {}", response);
        log.info("응답 메타데이터: {}", response.getResult().getMetadata());
        log.info("AI 답변: [{}]", answer);

        if (answer == null || answer.isBlank()) {
            return "MCP 도구는 정상적으로 실행됐지만 AI의 최종 답변이 비어 있습니다.";
        }

        return answer;
    }
}