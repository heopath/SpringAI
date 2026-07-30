package com.example.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import com.example.tool.BoomBarrierTools;
import com.example.tool.CarCheckTools;
import com.example.tool.DateTimeTools;
import com.example.tool.FileSystemTools;
import com.example.tool.HeatingSystemTools;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FileSystemToolsService {

	private ChatClient chatClient;
	
	@Autowired
	private FileSystemTools fileSystemTools; 
		
	public FileSystemToolsService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
		this.chatClient = chatClientBuilder
				.defaultAdvisors(
					MessageChatMemoryAdvisor.builder(chatMemory).build()
				)
				.defaultSystem("파일, 디렉토리 관련 질문은 반드시 도구를 사용하세요.")				
				.build();
	}

	public String chat(String question, String conversationId) {
		
		// LLM으로 요청하고 응답받기
		String answer = chatClient
						.prompt()						
						.user(question)
						.advisors(advisorSpec -> advisorSpec
								.param(ChatMemory.CONVERSATION_ID, conversationId)
						)
						.tools(fileSystemTools)
						.call()
						.content();
		return answer;
	}

}