package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.tool.RecommendMovieTools;

@Service
public class RecommendMovieToolsService {

    private final ChatClient chatClient;
    private final RecommendMovieTools recommendMovieTools;

    public RecommendMovieToolsService(
            ChatClient.Builder chatClientBuilder,
            RecommendMovieTools recommendMovieTools) {

        this.chatClient = chatClientBuilder.build();
        this.recommendMovieTools = recommendMovieTools;
    }

    public String chat(String question) {

        return chatClient
                .prompt()
                .system("""
                        당신은 사용자의 영화 취향을 분석하고 영화를 추천하는 AI입니다.

                        다음 순서로 작업하세요.
                        1. 질문에서 사용자 ID를 확인하세요.
                        2. 사용자 ID가 있으면 관람 영화 목록을 조회하세요.
                        3. 관람 영화 목록을 바탕으로 선호 장르를 판단하세요.
                        4. 판단한 장르로 영화 추천 도구를 호출하세요.
                        5. 사용자 ID가 없다면 먼저 사용자 ID를 물어보세요.
                        """)
                .user(question)
                .tools(recommendMovieTools)
                .call()
                .content();
    }
}