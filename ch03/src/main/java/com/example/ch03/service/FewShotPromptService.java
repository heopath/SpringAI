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
public class FewShotPromptService {

private final ChatClient chatClient;
	
	public FewShotPromptService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	
	public String prompt(String order) {
		
		String strprompt = """
				주문을 JSON 형식으로 반환하시오.
				추가 설명은 하지마세요.
				
				예시1: 
				작은피자 하나, 치즈랑 토마토 소스, 페퍼로니 주세요.
				
				JSON 응답:
				{
					"size": "small",
					"type": "normal",
					"ingredients": ["cheese", "tomato source", "pepperoni"]
				}
				
				고객주문 : %s""".formatted(order);
				
		Prompt prompt = new Prompt(strprompt);
		
		String answer = chatClient.prompt(prompt)
				.call()      
				.content();
		
		return answer;
	}

}
