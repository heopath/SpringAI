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
public class SelfConsistencyService {

private final ChatClient chatClient;
	
	public SelfConsistencyService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	

	public String promptStream(String complexProblem) {
		String promptText = """
				다음 논리 문제를 풀기 위해 3가지 서로 다른 추론 경로(서로 다른 관점의 풀이)를 독립적으로 작성해 보세요.
				그 후, 3가지 풀이 결과를 비교하여 가장 일관성 있고 다수결(Majority Vote)에 부합하는 최종 정답을 도출하세요.
				
				[출력 양식]
				- 풀이 경로 A:
				- 풀이 경로 B:
				- 풀이 경로 C:
				- 최종 결론 및 정답:
				
				문제: %s
				""".formatted(complexProblem);

		// .call() 대신 .stream()을 사용하여 Flux<String>을 리턴합니다.
		return chatClient.prompt()
				.user(promptText)
				.call()
				.content();
	}
}
