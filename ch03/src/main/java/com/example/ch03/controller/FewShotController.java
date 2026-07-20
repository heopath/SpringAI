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
import com.example.ch03.service.FewShotPromptService;
import com.example.ch03.service.ZeroShotPromptService;

import reactor.core.publisher.Flux;

// @Slf4j <- 과감하게 지워줍니다.
@Controller
public class FewShotController {

	// 2. 롬복 대신 순수 자바 코드로 log 변수를 직접 생성합니다.
	private static final Logger log = LoggerFactory.getLogger(FewShotController.class);

	private final FewShotPromptService service;
	
	public FewShotController(FewShotPromptService service) {
		this.service = service;
	}
	
	@GetMapping("/ai/few-shot-prompt")
	public String promptTemplate() {
		return "/few-shot-prompt";
	}
	
	@ResponseBody
	@PostMapping(value = "/ai/few-shot-prompt")
	public String fewshotprompt(@RequestParam("order") String order) {
		
		log.info("받은 피자 주문문장: {}", order);
		
		// 이제 퓨샷 서비스의 prompt가 정상적으로 호출됩니다.
		String result = service.prompt(order);
		
		log.info("퓨샷 반환 결과 JSON: {}", result);
		
		return result;
	}
	
}