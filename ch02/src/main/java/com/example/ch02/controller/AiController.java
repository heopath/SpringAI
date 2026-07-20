package com.example.ch02.controller;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class AiController {
	
	@Autowired
	private ChatModel chatModel;
	
	@PostMapping(
	        value = "/ai/chat",
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
	        produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter chat(@RequestParam("question") String question) {

		System.out.println("=== [서버 수신] 질문: " + question);
		
		// 타임아웃 5분
		SseEmitter emitter = new SseEmitter(300000L);
		
		emitter.onCompletion(() -> System.out.println("=== SSE 연결 종료 ==="));
		emitter.onTimeout(() -> emitter.complete());

        SystemMessage systemMessage = SystemMessage.builder()
                                        .text("사용자 질문에 한국어로 답변해야 됩니다.")
                                        .build();

        UserMessage userMessage = UserMessage.builder()
                                    .text(question)
                                    .build();

        // 💡 중요: 토큰 제한이나 옵션이 꼬여서 빈 스트림이 나오지 않도록 
        // maxTokens를 제외하고 모델명만 깔끔하게 주입해봅니다.
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                                    .model("gpt-4o-mini")
                                    .temperature(0.5)
                                    .build();

        Prompt prompt = Prompt.builder()
                                .messages(systemMessage, userMessage)
                                .chatOptions(chatOptions)
                                .build();

        // 비동기 스트림 호출
        Flux<ChatResponse> fluxResponse = chatModel.stream(prompt);
        
        // 전통적이고 가장 직관적인 subscribe 구조로 변경
        fluxResponse.subscribe(
            chatResponse -> {
                try {
                    if (chatResponse != null && chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null) {
                        String answer = chatResponse.getResult().getOutput().getText();
                        
                        if (answer != null && !answer.isEmpty()) {
                            System.out.print(answer); // 서버 콘솔에도 AI 글자가 찍히는지 확인용
                            emitter.send("data:" + answer + "\n\n");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("데이터 전송 오류: " + e.getMessage());
                }
            },
            error -> {
                System.err.println("=== AI 스트림 에러 발생 ===");
                error.printStackTrace();
                emitter.completeWithError(error);
            },
            () -> {
                System.out.println("\n=== AI 답변 스트림 완료 ===");
                emitter.complete();
            }
        );

        return emitter;
    }
}