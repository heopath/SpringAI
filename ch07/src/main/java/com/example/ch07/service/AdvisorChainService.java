package com.example.ch07.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.stereotype.Service;

import com.example.ch07.advisor.AdvisorA;
import com.example.ch07.advisor.AdvisorB;
import com.example.ch07.advisor.AdvisorC;

import reactor.core.publisher.Flux;

@Service
public class AdvisorChainService {

	private ChatClient chatClient;
	
	public AdvisorChainService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
										.defaultAdvisors(
											new AdvisorA(),
											new AdvisorB()
										)
										.build();
	}
	
	public String call(String question) {		
		String answer = chatClient.prompt()
								.advisors(new AdvisorC())
								.user(question)
								.call()
								.content();
		
		return answer;
	}
	
	public Flux<String> stream(String question) {
		
		Flux<String> answer = chatClient.prompt()
				.advisors(new AdvisorC())
				.user(question)
				.stream()
				.content();

		return answer;		
	}
	
}
