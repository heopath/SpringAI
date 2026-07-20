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
public class ZeroShotPromptService {

private final ChatClient chatClient;
	
	public ZeroShotPromptService(ChatClient.Builder chatClientBuilder) {
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
	
	public String prompt(String review) {
		// 템플릿에 사용자가 입력한 리뷰 내용을 채워 넣습니다.
		String userText = promptTemplate.render(Map.of("review", review));
		
		return chatClient.prompt()
				.user(userText)
				.call()      // stream() 대신 call()을 사용하여 동기식 호출
				.content();  // 결과로 나온 텍스트 한 통을 통째로 리턴 (String)
	}

}
