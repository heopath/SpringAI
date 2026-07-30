package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.tool.ExceptionHandlingTools;

@Service
public class ExceptionHandlingService {

    private final ChatClient chatClient;
    private final ExceptionHandlingTools exceptionHandlingTools;

    public ExceptionHandlingService(
            ChatClient.Builder chatClientBuilder,
            ExceptionHandlingTools exceptionHandlingTools) {

        this.chatClient = chatClientBuilder.build();
        this.exceptionHandlingTools = exceptionHandlingTools;
    }

    public String chat(String question) {

        return chatClient
                .prompt()
                .system("""
                        당신은 사용자의 영화 관람 이력을 확인하고
                        영화를 추천하는 AI입니다.

                        규칙:
                        1. 사용자 ID가 있으면 먼저 관람 영화 목록을 조회하세요.
                        2. 도구에서 사용자 ID가 존재하지 않는다는
                           오류를 받으면 다른 도구를 호출하지 마세요.
                        3. 오류가 발생하면 정확히 다음 문장만 출력하세요.
                           [LLM] 질문을 처리할 수 없습니다.
                        """)
                .user(question)
                .tools(exceptionHandlingTools)
                .call()
                .content();
    }
}