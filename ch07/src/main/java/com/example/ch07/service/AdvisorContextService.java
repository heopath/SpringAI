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
import com.example.ch07.advisor.MaxCharLengthAdvisor;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
@Service
public class AdvisorContextService {

	private ChatClient chatClient;
	
	public AdvisorContextService(ChatClient.Builder chatClientBuilder) {
		
		
		this.chatClient = chatClientBuilder
							.defaultAdvisors(
								new MaxCharLengthAdvisor(Ordered.HIGHEST_PRECEDENCE),
								new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE) 
							)
							.build();
	}
	
	public String call(String question) {	

		String answer = chatClient.prompt()
								//.advisors(advisor -> advisor.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 100))
								.user(question)
								.call()
								.content();
		
		return answer;
	}
	
}








