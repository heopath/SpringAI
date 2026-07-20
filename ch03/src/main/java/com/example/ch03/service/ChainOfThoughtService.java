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
public class ChainOfThoughtService {

private final ChatClient chatClient;
	
	public ChainOfThoughtService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	

	private final PromptTemplate promptTemplate = PromptTemplate.builder()
			.template(
			"""
			영화 리뷰를 [긍정적, 중립적, 부정적] 중에서 하나로 분류해주세요.
			레이블만 반환 하세요.
			리뷰: {review}
			"""
			).build();
	
	public Flux<String> promptStream(String mathProblem) {
		String promptText = """
				문제를 단계별로 차근차근 생각(Chain of Thought)하여 해결해 주세요.
				
				[출력 양식]
				1. 문제 분석:
				2. 풀이 과정 (단계별):
				3. 최종 정답:
				
				문제: %s
				""".formatted(mathProblem);

		return chatClient.prompt()
				.user(promptText)
				.stream() // 실시간 스트리밍 처리
				.content();
	}

}
