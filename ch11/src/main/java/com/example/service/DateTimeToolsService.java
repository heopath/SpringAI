package com.example.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DateTimeToolsService {

    private final ChatClient chatClient;

    public DateTimeToolsService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String chat(String question) {
        return chatClient.prompt()
                .user(question)
                // @Tool이 붙은 메서드를 LLM에 도구로 제공
                .tools(this)
                .call()
                .content();
    }

    @Tool(description = "사용자 지역의 현재 날짜와 시간을 ISO-8601 형식으로 조회합니다.")
    public String getCurrentDateTime() {
        String currentDateTime = LocalDateTime.now(
                LocaleContextHolder.getTimeZone().toZoneId()
        ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        log.info("현재 날짜 및 시간 조회: {}", currentDateTime);

        return currentDateTime;
    }

    @Tool(description = "ISO-8601 형식으로 전달받은 날짜와 시간에 알람을 설정합니다.")
    public String setAlarm(
            @ToolParam(description = "알람 날짜와 시간. 예: 2026-07-28T16:30:00")
            String time) {

        LocalDateTime alarmTime = LocalDateTime.parse(
                time,
                DateTimeFormatter.ISO_DATE_TIME
        );

        log.info("알람 설정: {}", alarmTime);

        // 현재는 실제 알람이 아니라 도구 호출 연습용
        return "알람이 %s로 설정되었습니다."
                .formatted(
                        alarmTime.format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy년 MM월 dd일 HH시 mm분"
                                )
                        )
                );
    }
}