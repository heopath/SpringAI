package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import com.example.tool.BoomBarrierTools;
import com.example.tool.CarCheckTools;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BoomBarrierToolsService {

    private final ChatClient chatClient;
    private final BoomBarrierTools boomBarrierTools;
    private final CarCheckTools carCheckTools;

    public BoomBarrierToolsService(
            ChatClient.Builder chatClientBuilder,
            BoomBarrierTools boomBarrierTools,
            CarCheckTools carCheckTools) {

        this.chatClient = chatClientBuilder.build();
        this.boomBarrierTools = boomBarrierTools;
        this.carCheckTools = carCheckTools;
    }

    public String chat(String contentType, byte[] bytes) {

        log.info("BoomBarrierToolsService 실행");
        log.info("contentType: {}", contentType);
        log.info("image size: {}", bytes.length);

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "이미지 Content-Type이 없습니다."
            );
        }

        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();

        log.info("Media 생성 완료");

        UserMessage userMessage = UserMessage.builder()
                .text("""
                        이미지에서 차량 번호를 인식하세요.

                        차량 번호 형식:
                        숫자 2~3개 + 한글 1자 + 숫자 4개

                        등록된 번호이면 차단기를 올리고,
                        미등록 번호이거나 인식하지 못하면
                        차단기를 내리세요.

                        반드시 차량 조회 도구를 먼저 호출하세요.

                        최종 답변:
                        차단기 올림 또는 차단기 내림
                        """)
                .media(media)
                .build();

        log.info("Ollama 요청 시작");

        String answer = chatClient
                .prompt()
                .messages(userMessage)
                .tools(carCheckTools, boomBarrierTools)
                .call()
                .content();

        log.info("Ollama 응답 완료: {}", answer);

        return answer;
    }
}