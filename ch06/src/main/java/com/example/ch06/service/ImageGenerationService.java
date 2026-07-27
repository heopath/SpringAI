package com.example.ch06.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

@Service
public class ImageGenerationService {
	
	private ChatClient chatClient;
	private ImageModel imageModel; 
	
	public ImageGenerationService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
		this.chatClient = chatClientBuilder.build();
		this.imageModel = imageModel;		
	}	
	
	public String generation(String description) {
		
		// 이미지 메시지 작성
		ImageMessage imageMessage = new ImageMessage(description);
		
		
		// 이미지 옵션 설정
		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
											.model("gpt-image-1")
											.quality("low")
											.width(1024)
											.height(1024)
											.n(1)		// 생성할 이미지 갯수				
											.build();
		
		// 프롬프트 생성
		List<ImageMessage> imageMessageList = List.of(imageMessage); 
		ImagePrompt imagePrompt = new ImagePrompt(imageMessageList, imageOptions);
		
		// 모델 요청 및 응답
		ImageResponse imageResponse = imageModel.call(imagePrompt);			
		
		// base64로 인코딩된 이미지 문자열 반환
		String b64Json = imageResponse.getResult().getOutput().getB64Json();
		
		
		return b64Json;
	}

}






