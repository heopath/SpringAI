package com.example.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatService {

	private final ChatClient chatClient;

	public ChatService(
			ChatClient.Builder chatClientBuilder,
			ToolCallbackProvider mcpToolProvider) {

		this.chatClient = chatClientBuilder
				.defaultTools(mcpToolProvider)
				.build();
	}

	public String chat(String question) {

		log.info("사용자 질문: {}", question);

		String answer = chatClient.prompt()
				.user(question)
				.call()
				.content();

		log.info("AI 답변: {}", answer);

		return answer;
	}
}