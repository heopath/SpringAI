package com.example.ch02.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openai.models.completions.CompletionCreateParams.Prompt;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
public class AiService {
	
	private final ChatClient chatClient;
	
	public AiService(ChatClient.Builder chatClientBuilder) {
		chatClient = chatClientBuilder.build();
	}
	
	private PromptTemplate systemTemplate = SystemPromptTemplate.builder()
			.template("""
					답변을 생성할때 HTML과 CSS를 사용해서 파란색 글자로 출력하시오.
					<span> 태그 안에 들어갈 내용만 출력하시오.
					""")
					.build();
	
	private PromptTemplate userTemplate = SystemPromptTemplate.builder()
			.template("다음 질문을 {language}로 답변하시오.\n질문: {statement}")
			.build();

	
	public Flux<String> promptTemplate1(String statement, String language) {
		
		Prompt prompt = userTemplate.create(Map.of("statement", statement, "language", language));
		
		Flux<String> response = chatClient.prompt(prompt).stream().content();
		
		return null;
	}
	public Flux<String> promptTemplate2(String statement, String language) {
		return null;
	}
	public Flux<String> promptTemplate3(String statement, String language) {
		return null;
	}
	public Flux<String> promptTemplate4(String statement, String language) {
		return null;
	}
	
}
