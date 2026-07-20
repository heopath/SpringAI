package com.example.ch03.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// 서비스 임포트
import com.example.ch03.service.RoleAssignmentService;
import com.example.ch03.service.StepBackPromptService;

import reactor.core.publisher.Flux;

import com.example.ch03.service.ChainOfThoughtService;
import com.example.ch03.service.SelfConsistencyService;

@Controller
public class PromptExtensionController {

	private static final Logger log = LoggerFactory.getLogger(PromptExtensionController.class);

	// 필요한 서비스들 선언
	private final RoleAssignmentService roleAssignmentService;
	private final StepBackPromptService stepBackPromptService;
	private final ChainOfThoughtService chainOfThoughtService;
	private final SelfConsistencyService selfConsistencyService;

	// 롬복 에러 방지를 위한 순수 자바 생성자 주입
	public PromptExtensionController(
			RoleAssignmentService roleAssignmentService,
			StepBackPromptService stepBackPromptService,
			ChainOfThoughtService chainOfThoughtService,
			SelfConsistencyService selfConsistencyService) {
		this.roleAssignmentService = roleAssignmentService;
		this.stepBackPromptService = stepBackPromptService;
		this.chainOfThoughtService = chainOfThoughtService;
		this.selfConsistencyService = selfConsistencyService;
	}

	// ==========================================
	// 1. Role Assignment (역할 지정)
	// ==========================================
	@GetMapping("/ai/role-assignment")
	public String roleAssignmentForm() {
		return "/role-assignment"; // HTML 파일명
	}

	@ResponseBody
	@PostMapping(
			value = "/ai/role-assignment",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_NDJSON_VALUE // 스트리밍 응답 규격 선언
	)

	public Flux<String> roleAssignment(@RequestParam("requirements") String requirements) {
		log.info("Requirements: {}", requirements);
		
		// 고정 페르소나 설정 ("여행 가이드" 역할 부여)
		String defaultRole = "여행 가이드"; 
		
		// 리턴 타입을 Flux<String>으로 변경하고 서비스 메서드도 stream()을 쓰도록 연결합니다.
		return roleAssignmentService.promptStream(defaultRole, requirements);
	}

	// ==========================================
	// 2. Step-Back Prompting (스텝백)
	// ==========================================
	@GetMapping("/ai/step-back-prompt")
	public String stepBackForm() {
		return "/step-back-prompt"; // HTML 파일명
	}

	@ResponseBody
	@PostMapping("/ai/step-back-prompt")
	public String stepBack(@RequestParam("question") String question) {
		log.info("Step-Back Question: {}", question);
		return stepBackPromptService.prompt(question);
	}

	// ==========================================
	// 3. Chain of Thought (생각의 사슬)
	// ==========================================
	@GetMapping("/ai/chain-of-thought")
	public String chainOfThoughtForm() {
		return "/chain-of-thought"; // HTML 파일명
	}

	@ResponseBody
	@PostMapping("/ai/chain-of-thought")
	public Flux<String> chainOfThought(@RequestParam("question") String question) {
		log.info("CoT Problem: {}", question);
		return chainOfThoughtService.promptStream(question);
	}

	// ==========================================
	// 4. Self-Consistency (자기 일관성)
	// ==========================================
	@GetMapping("/ai/self-consistency")
	public String selfConsistencyForm() {
		return "/self-consistency"; 
	}

	@ResponseBody
	@PostMapping(value = "/ai/self-consistency")
	// 화면의 input/textarea name이 "requirements"인 경우 아래처럼 매핑합니다.
	public String selfConsistency(@RequestParam("content") String question) {
		log.info("Self-Consistency 스트리밍 요청 - 문제: {}", question);
		
		// 서비스의 스트리밍 메서드를 호출하여 반환합니다.
		return selfConsistencyService.promptStream(question);
	}
}