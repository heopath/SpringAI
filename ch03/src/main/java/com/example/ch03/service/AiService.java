package com.example.ch03.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

// import com.openai... Prompt 클래스는 충돌 방지를 위해 반드시 삭제해야 합니다!
import reactor.core.publisher.Flux;

@Service
public class AiService {
	
	private final ChatClient chatClient;
	
	public AiService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	// 가장 직관적이고 표준적인 생성자 방식으로 템플릿 선언
	private final SystemPromptTemplate systemTemplate = new SystemPromptTemplate(
			"""
			답변을 생성할때 HTML과 CSS를 사용해서 파란색 글자로 출력하시오.
			<span> 태그 안에 들어갈 내용만 출력하시오.
			""");
	
	private final PromptTemplate userTemplate = new PromptTemplate("다음 질문을 {language}로 답변하시오.\n질문: {statement}");

	
	/**
	 * 방법 1: System Message와 User Message를 리스트로 묶어 정석대로 Prompt 객체를 만드는 방법
	 */
	public Flux<String> promptTemplate1(String statement, String language) {
		
		// 1. 시스템 메시지와 유저 메시지를 각각 생성
		Message systemMessage = systemTemplate.createMessage();
		Message userMessage = userTemplate.createMessage(Map.of("statement", statement, "language", language));
		
		// 2. 두 메시지를 결합하여 Spring AI 표준 Prompt 객체 생성
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		
		// 3. chatClient에 프롬프트를 주입하고 스트림 반환
		return chatClient.prompt(prompt)
				.stream()
				.content();
	}

	/**
	 * 방법 2: ChatClient의 Fluent API(체이닝)를 활용하여 텍스트로 바로 주입하는 방법 (가장 추천)
	 */
	public Flux<String> promptTemplate2(String statement, String language) {
		
		String systemText = systemTemplate.render();
		String userText = userTemplate.render(Map.of("statement", statement, "language", language));
		
		return chatClient.prompt()
				.system(systemText)
				.user(userText)
				.stream()
				.content();
	}

	/**
	 * 방법 3: ChatClient 빌더 내부에서 전역(Default) 시스템 설정을 고정하고 유저 메시지만 처리하는 방법
	 */
	public Flux<String> promptTemplate3(String statement, String language) {
		
		// userTemplate으로 생성한 프롬프트 객체를 이용해 바로 요청
		Prompt userPrompt = userTemplate.create(Map.of("statement", statement, "language", language));
		
		return chatClient.prompt(userPrompt)
				.stream()
				.content();
		// 주의: 이 방식을 제대로 쓰려면 생성자 등에서 chatClientBuilder.defaultSystem(...)을 설정해 두면 편합니다.
	}

	/**
	 * 방법 4: 템플릿 없이 ChatClient 내부에서 인라인 코드로 프롬프트를 직관적으로 처리하는 방법
	 */
	public Flux<String> promptTemplate4(String statement, String language) {
		
		return chatClient.prompt()
				.system("답변을 생성할때 HTML과 CSS를 사용해서 파란색 글자로 출력하시오. <span> 태그 안에 들어갈 내용만 출력하시오.")
				.user(u -> u.text("다음 질문을 {language}로 답변하시오.\n질문: {statement}")
						   .params(Map.of("statement", statement, "language", language)))
				.stream()
				.content();
	}
}