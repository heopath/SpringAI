package com.example.ch07.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.example.ch07.advisor.AdvisorA;
import com.example.ch07.advisor.AdvisorB;
import com.example.ch07.advisor.AdvisorC;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
@Service
public class AdvisorSafeGuardService {

	private ChatClient chatClient;
	
	public AdvisorSafeGuardService(ChatClient.Builder chatClientBuilder) {
		
		SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
				List.of("욕", "욕설", "폭탄", "폭력"),
				"해당 질문은 민감한 콘텐츠 요청이므로 답변할 수 없습니다.",
				Ordered.HIGHEST_PRECEDENCE + 1
				); 
		
		
		this.chatClient = chatClientBuilder
							.defaultAdvisors(
								safeGuardAdvisor,
								new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE) 
							)
							.build();
	}
	
	public String call(String question) {	

		String answer = chatClient.prompt()								
								.user(question)
								.call()
								.content();
		
		return answer;
	}
	
	
	
}







