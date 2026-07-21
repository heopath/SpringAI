package com.example.ch04.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class SystemMessageService {

    private final NvidiaApiService nvidiaApiService;

    public String chat(String question) {

        String systemMessage = """
                당신은 전문 여행 가이드입니다.

                사용자가 도시를 질문하면
                관광지와 여행 정보를 친절하게 설명하세요.

                답변은 반드시 한국어로 작성하세요.
                """;

        String answer =
                nvidiaApiService.callWithSystemMessage(
                        systemMessage,
                        question
                );

        log.info(
                "System Message 응답: {}",
                answer
        );

        return answer;
    }
}