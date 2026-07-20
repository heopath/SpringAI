package com.example.ch03.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class RoleAssignmentService {

private final ChatClient chatClient;
	
	public RoleAssignmentService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	
	public Flux<String> promptStream(String role, String question) {
		String systemMessage = "당신의 역할은 [%s] 입니다. 해당 역할에 어울리는 어조와 전문성을 가지고 답변하세요.".formatted(role);

		return chatClient.prompt()
				.system(systemMessage)
				.user(question)
				.stream() // call() 대신 stream() 사용
				.content();
	}
}
