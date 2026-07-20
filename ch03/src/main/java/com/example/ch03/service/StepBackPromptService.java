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
public class StepBackPromptService {

private final ChatClient chatClient;
	
	public StepBackPromptService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	public String prompt(String question) {
		String promptText = """
				질문: %s
				
				위 질문에 바로 답하지 마십시오. 
				먼저 이 질문의 근저에 있는 핵심적인 '상위 개념'이나 '기본 원리'가 무엇인지 한 단계 물러서서(Step-back) 파악한 뒤, 
				그 원리를 바탕으로 질문에 대해 단계적으로 논리적인 답변을 작성해 주세요.
				""".formatted(question);

		return chatClient.prompt()
				.user(promptText)
				.call()
				.content();
	}

}
