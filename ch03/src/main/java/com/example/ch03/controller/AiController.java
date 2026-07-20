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

import reactor.core.publisher.Flux;

// @Slf4j <- 과감하게 지워줍니다.
@Controller
public class AiController {

	// 2. 롬복 대신 순수 자바 코드로 log 변수를 직접 생성합니다.
	private static final Logger log = LoggerFactory.getLogger(AiController.class);

	private final AiService service;
	
	public AiController(AiService service) {
		this.service = service;
	}
	
	@GetMapping("/ai/prompt-template")
	public String promptTemplate() {
		return "/prompt-template";
	}
	
	@ResponseBody
	@PostMapping(
			value = "/ai/prompt-template",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE
	)
	public Flux<String> promptTemplate(@RequestParam("statement") String statement,
	                                   @RequestParam("language") String language) {
		
		// 이제 log.info 코드가 에러 없이 정상 작동합니다.
		log.info("statement : {}", statement);
		log.info("language : {}", language);
		
		// return service.promptTemplate1(statement, language);
		return service.promptTemplate2(statement, language);
		// return service.promptTemplate3(statement, language);
		// return service.promptTemplate4(statement, language);

	}
}