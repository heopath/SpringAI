package com.example.ch09.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class VectorStoreService {

    private final ChatClient chatClient;

    public VectorStoreService(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        VectorStoreChatMemoryAdvisor
                                .builder(vectorStore)
                                .build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public String chat(String question, String conversationId) {

        log.info("conversationId: {}", conversationId);
        log.info("question: {}", question);

        return chatClient.prompt()
                .user(question)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId))
                .call()
                .content();
    }
}