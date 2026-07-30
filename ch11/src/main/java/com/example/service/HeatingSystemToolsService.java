package com.example.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.example.tool.HeatingSystemTools;

@Service
public class HeatingSystemToolsService {

    private final ChatClient chatClient;
    private final HeatingSystemTools heatingSystemTools;

    public HeatingSystemToolsService(
            ChatClient.Builder chatClientBuilder,
            HeatingSystemTools heatingSystemTools) {

        this.chatClient = chatClientBuilder.build();
        this.heatingSystemTools = heatingSystemTools;
    }

    public String chat(String question) {

        return chatClient
                .prompt()
                .system("""
                        당신은 난방 시스템을 관리하는 AI입니다.

                        다음 순서로 작업하세요.
                        1. 먼저 현재 온도 조회 도구를 호출하세요.
                        2. 현재 온도가 사용자가 원하는 온도 이상이면 난방을 중지하세요.
                        3. 현재 온도가 사용자가 원하는 온도보다 낮으면 난방을 가동하세요.
                        4. 도구 실행 결과와 현재 온도를 한국어로 알려주세요.
                        """)
                .user(question)
                .tools(heatingSystemTools)
                .toolContext(
                        Map.of(
                                "controlKey",
                                "heatingSystemKey"
                        )
                )
                .call()
                .content();
    }
}