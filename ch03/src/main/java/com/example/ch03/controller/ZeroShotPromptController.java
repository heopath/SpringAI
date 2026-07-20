package com.example.ch03.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // 1. 로거 관련 임포트 추가
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ch03.service.AiService;
import com.example.ch03.service.ZeroShotPromptService;

import reactor.core.publisher.Flux;

// @Slf4j <- 과감하게 지워줍니다.
@Controller
public class ZeroShotPromptController {

	// 2. 롬복 대신 순수 자바 코드로 log 변수를 직접 생성합니다.
	private static final Logger log = LoggerFactory.getLogger(ZeroShotPromptController.class);

	private final ZeroShotPromptService service;
	
	public ZeroShotPromptController(ZeroShotPromptService service) {
		this.service = service;
	}
	
	@GetMapping("/ai/zero-shot-prompt")
	public String promptTemplate() {
		return "/zero-shot-prompt";
	}
	
	@ResponseBody
	@PostMapping(value = "/ai/zero-shot-prompt")
	public String zeroShotPrompt(@RequestParam("review") String review) {
		
		log.info("입력된 영화 리뷰: {}", review);
		
		// 서비스의 prompt 메서드를 호출하여 "긍정적", "부정적", "중립적" 중 하나의 결과를 받습니다.
		String result = service.prompt(review);
		
		log.info("분류 결과: {}", result);
		
		return result;
	}
	
}