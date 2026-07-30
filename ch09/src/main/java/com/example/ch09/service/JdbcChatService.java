package com.example.ch09.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JdbcChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public JdbcChatService(
            ChatMemory chatMemory,
            ChatClient.Builder chatClientBuilder) {

        this.chatMemory = chatMemory;

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor
                                .builder(chatMemory)
                                .build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public String chat(String question, String conversationId) {

        String answer = chatClient.prompt()
                .user(question)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId))
                .call()
                .content();

        log.info("conversationId: {}", conversationId);
        log.info("stored messages: {}",
                chatMemory.get(conversationId));

        return answer;
    }
}