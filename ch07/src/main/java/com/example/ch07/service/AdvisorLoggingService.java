package com.example.ch07.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

import com.example.ch07.advisor.AdvisorA;
import com.example.ch07.advisor.AdvisorB;
import com.example.ch07.advisor.AdvisorC;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
@Service
public class AdvisorLoggingService {

	private ChatClient chatClient;
	
	public AdvisorLoggingService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder
							.defaultAdvisors(
								// propertiese파일에 logging.level DEBUG 설정을 반드시 해야됨	
								new SimpleLoggerAdvisor() 
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








