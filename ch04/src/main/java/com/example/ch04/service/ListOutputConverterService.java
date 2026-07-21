package com.example.ch04.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ListOutputConverterService {

	private ChatClient chatClient;
	
	public ListOutputConverterService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	public List<String> convertlowLevel(String city) {
		
		// 출력 변환기 생성
		ListOutputConverter converter = new ListOutputConverter();
		
		// 프롬프트 템플릿 생성
		PromptTemplate prompttemplate = PromptTemplate.builder()
										.template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
										.build();
		
		// 프롬프트 생성
		Prompt prompt = prompttemplate.create(
					Map.of("city", city, "format", converter.getFormat())
					); 
		
		// LLM 요청 및 응답
		String answer = chatClient.prompt(prompt).call().content();
		log.info(answer);
		
		// Converter로 List 변환
		List<String> answerList = converter.convert(answer);
		log.info(answerList);
		
		
		return null;
	}
	
	public List<String> converthighLevel() {
		
		
		return null;
	}
	
}
